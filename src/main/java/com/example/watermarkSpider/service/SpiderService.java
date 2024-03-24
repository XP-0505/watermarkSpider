package com.example.watermarkSpider.service;

import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface SpiderService {
    /**
     * 爬取视频
     * @param req
     * @return
     */
    BaseResp sipder(BaseReq req);

    BaseResp downloadFile(BaseReq req);

    ResponseEntity<Resource> findFile(String fileName);


}
