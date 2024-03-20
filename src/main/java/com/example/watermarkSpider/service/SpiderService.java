package com.example.watermarkSpider.service;

import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;

public interface SpiderService {
    /**
     * 爬取视频
     * @param req
     * @return
     */
    BaseResp sipder(BaseReq req);

}
