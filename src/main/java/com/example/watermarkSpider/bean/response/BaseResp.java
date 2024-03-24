package com.example.watermarkSpider.bean.response;

import lombok.Data;


@Data
public class BaseResp {
    private String code = "200";
    private String msg = "success";
    private String data;
    private SpecificInfo specificInfo;

}
