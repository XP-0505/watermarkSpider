package com.example.watermarkSpider.util.WyyAes;

import lombok.Data;

/**
 * 表单参数
 */
@Data
public class FormParam {

    private String params;

    private String encSecKey;

    public FormParam(String params, String encSecKey) {
        this.params = params;
        this.encSecKey = encSecKey;
    }
}
