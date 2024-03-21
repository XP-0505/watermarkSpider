package com.example.watermarkSpider.controller;

import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
