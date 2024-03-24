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

    /**
     * 处理文件下载请求。
     *
     * @param req 包含下载请求信息的对象，来自请求体。
     * @return 响应下载结果的基类响应对象，包括成功与否、错误码等信息。
     */
    @RequestMapping("/download")
    @ResponseBody
    public BaseResp download(@RequestBody BaseReq req) {
        // 调用spiderService处理下载请求，并返回下载结果
        BaseResp baseResp = spiderService.downloadFile(req);
        return baseResp;
    }

    /**
     * 根据文件名查找文件。
     *
     * @param filename 通过URL路径变量传递的文件名
     * @return 返回一个包含文件资源的ResponseEntity对象
     */
    @RequestMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> findFile(@PathVariable("filename") String filename) {
        // 调用spiderService来查找并返回指定文件名的文件
        return spiderService.findFile(filename);
    }



}
