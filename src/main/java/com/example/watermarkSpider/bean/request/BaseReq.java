package com.example.watermarkSpider.bean.request;

import lombok.Data;

@Data
public class BaseReq {
    //请求参数
    private String data;
    //请求类型
    private String type;

    private String itemId;
}
