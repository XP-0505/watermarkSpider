package com.example.watermarkSpider.service.impl;

import com.example.watermarkSpider.bean.request.BaseReq;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.service.SpiderService;
import com.example.watermarkSpider.util.Dy;
import com.example.watermarkSpider.util.Wb;
import com.example.watermarkSpider.util.Xhs;
import org.springframework.stereotype.Service;

@Service
public class SpiderServiceImpl implements SpiderService {


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
                }else if (data.contains("weibo")){
                    baseResp = Wb.video(data);
                }
                // 处理图片类型请求
            } else if ("image".equals(type)) {
                // 根据图片来源调用不同的处理方法
                if (data.contains("抖音")) {
                    baseResp = Dy.image(data);
                } else if (data.contains("小红书")) {
                    baseResp = Xhs.image(data);
                }else if (data.contains("weibo")){
                    baseResp = Wb.image(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            baseResp.setCode("200");
            baseResp.setMsg("未找到资源，请检查链接或链接类型！");
        }finally {
            if (baseResp.getSpecificInfo() == null){
                baseResp.setCode("200");
                baseResp.setMsg("未找到资源，请检查链接或链接类型！");
            }
        }

        return baseResp;
    }
}
