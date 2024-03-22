package com.example.watermarkSpider.controller;

import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.watermarkSpider.util.Const.filePath;

@Controller
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    /**
     * 处理爬虫请求的控制器方法。
     *
     * @param req 请求体，包含爬虫任务的详细要求或参数。
     * @return 返回爬虫任务的处理结果，封装在BaseResp对象中。
     */
    @RequestMapping("/specificInfo")
    @ResponseBody
    public BaseResp spider(@RequestBody BaseReq req) {
        // 调用spiderService中的spider方法处理请求，并返回处理结果
        BaseResp baseResp = spiderService.sipder(req);
        return baseResp;
    }

    @RequestMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> file(@PathVariable("filename") String filename) {
        // 构造文件路径
        String newFilePath = filePath;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            newFilePath = "D:" + newFilePath;
        }
        Path filePath = Paths.get(newFilePath, filename);
        File file = filePath.toFile();
        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }
        // 读取文件内容
        try {
            Resource resource = new FileSystemResource(file);
            // 设置响应头信息
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream");

            // 返回文件内容作为响应体
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace(); // 实际应用中应使用更合适的错误处理
            return ResponseEntity.internalServerError().build();
        }
    }
}
