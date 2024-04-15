package com.example.watermarkSpider.service.impl;

import com.example.watermarkSpider.bean.VideoInfo;
import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.config.ServerConfig;
import com.example.watermarkSpider.mapper.VideoInfoRepository;
import com.example.watermarkSpider.service.SpiderService;
import com.example.watermarkSpider.util.*;
import org.springframework.data.domain.Example;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.example.watermarkSpider.util.Const.filePath;

@Service
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private VideoInfoRepository videoInfoRepository;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 处理爬虫请求的通用方法。
     * 根据请求类型（视频或图片）和请求数据来源（抖音或小红书）来调用相应的处理方法。
     *
     * @param req 包含请求数据和类型信息的BaseReq对象。
     * @return 根据处理结果生成的BaseResp对象。
     */
    @Override
    public BaseResp sipder(BaseReq req) {
        BaseResp baseResp = new BaseResp();
        String data = req.getData();
        String type = req.getType();
        // 处理视频类型请求
        try {
            if ("video".equals(type)) {
                // 根据视频来源调用不同的处理方法
                if (data.contains("抖音")) {
                    baseResp = Dy.video(data);
                } else if (data.contains("小红书")) {
                    baseResp = Xhs.video(data);
                } else if (data.contains("weibo")) {
                    baseResp = Wb.video(data);
                } else if (data.contains("网易云")) {
                    baseResp = Wyy.video(data);
                } else if (data.contains("QQ音乐")) {
                    baseResp = QQMusic.video(data);
                } else if (data.contains("火山")) {
                    baseResp = Hs.video(data);
                }
                // 处理图片类型请求
            } else if ("image".equals(type)) {
                // 根据图片来源调用不同的处理方法
                if (data.contains("抖音")) {
                    baseResp = Dy.image(data);
                } else if (data.contains("小红书")) {
                    baseResp = Xhs.image(data);
                } else if (data.contains("weibo")) {
                    baseResp = Wb.image(data);
                } else if (data.contains("火山")) {
                    baseResp = Hs.image(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResp.setCode("201");
            baseResp.setMsg("未找到资源，请检查链接或链接类型！");
        } finally {
            if (baseResp.getSpecificInfo() == null) {
                baseResp.setCode("201");
                baseResp.setMsg("未找到资源，请检查链接或链接类型！");
            }
        }

        return baseResp;
    }

    @Override
    public BaseResp downloadFile(BaseReq req) {
        BaseResp baseResp = new BaseResp();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setItemId(req.getItemId());
        Example<VideoInfo> example = Example.of(videoInfo);
        Optional<VideoInfo> optional = videoInfoRepository.findOne(example);
        if (optional.isPresent()) {
            VideoInfo videoInfo1 = optional.get();
            String fileName = videoInfo1.getVideoName();
            // 构造文件路径
            String newFilePath = filePath;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                newFilePath = "D:" + newFilePath;
            }
            Path filePath = Paths.get(newFilePath, fileName);
            // 检查文件是否存在
            if (!Files.exists(filePath)) {
                videoInfoRepository.delete(videoInfo1);
                saveFile(req);
            } else {
                baseResp.setData(videoInfo1.getVideoUrl());
            }
        } else {
            String newVideoUrl = saveFile(req);
            baseResp.setData(newVideoUrl);
        }
        return baseResp;
    }

    /**
     * 保存文件到服务器并返回其URL
     *
     * @param req 包含文件数据和类型的请求对象
     * @return 保存后文件的URL
     */
    public String saveFile(BaseReq req) {
        // 从请求中获取数据和类型
        String data = req.getData();
        String type = req.getType();

        // 生成当前时间戳作为文件名的一部分
        String datetime = String.valueOf(System.currentTimeMillis());
        String fileName = datetime + ".mp4"; // 默认文件名为时间戳加扩展名

        // 下载文件并获取新视频的URL
        String newVideoUrl = DownloadUtil.download(data, type, fileName);

        // 生成完整的视频URL
        String url = serverConfig.getUrl();
        newVideoUrl = url + "/spider/file/" + fileName;

        // 创建视频信息对象并填充数据
        VideoInfo videoInfo1 = new VideoInfo();
        videoInfo1.setItemId(req.getItemId());
        videoInfo1.setVideoUrl(newVideoUrl);
        videoInfo1.setVideoName(fileName);

        // 将视频信息保存到数据库
        videoInfoRepository.save(videoInfo1);

        return newVideoUrl; // 返回视频的URL
    }


    /**
     * 查找并返回指定名称的文件。
     *
     * @param fileName 需要查找的文件名称。
     * @return 如果文件存在且可读，则返回包含文件内容的ResponseEntity；如果文件不存在或不可访问，则返回404状态码的ResponseEntity；如果读取文件时发生错误，则返回500状态码的ResponseEntity。
     */
    @Override
    public ResponseEntity<Resource> findFile(String fileName) {
        // 根据操作系统类型构造文件的完整路径
        String newFilePath = filePath;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            newFilePath = "D:" + newFilePath; // 为Windows系统添加驱动器路径
        }
        Path filePath = Paths.get(newFilePath, fileName); // 完善文件路径
        File file = filePath.toFile();

        // 检查文件是否存在且是一个文件
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build(); // 文件不存在时返回404状态码
        }

        // 读取文件内容，并设置响应头信息
        try {
            Resource resource = new FileSystemResource(file); // 将文件包装成资源对象
            // 探测并设置文件的MIME类型
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\""); // 设置文件下载名称
            headers.add(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream"); // 设置内容类型
            // 构造并返回包含文件内容的响应体
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length()) // 设置响应体长度
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace(); // 处理读取文件时的IO异常
            return ResponseEntity.internalServerError().build(); // 发生错误时返回500状态码
        }
    }

}
