package com.example.watermarkSpider.util.WyyAes;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class MvParam {

    /**
     * 音乐ID数组
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 编码类型
     */
    @JSONField(name = "encodeType", defaultValue = "aac")
    private String encodeType;

    @JSONField(name = "r")
    private String r;

    @JSONField(name = "size")
    private String size;
    /**
     * CSRF标志
     */
    @JSONField(name = "csrf_token", defaultValue = "")
    private String csrfToken;

}
