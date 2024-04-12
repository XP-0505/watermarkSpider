package com.example.watermarkSpider.util.WyyAes;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter.Feature;

/**
 * 加密工具类
 */
public class CryptoUtil {

    /**
     * AES加密公共秘钥
     */
    private static final String aesEncryptKey = "0CoJUm6Qyw8W8jud";

    public static <T> FormParam encrypt(T data) {
        // 查询对象转JSON字符串
        String searchParamStr = JSON.toJSONString(data, Feature.WriteNullStringAsEmpty);

        // 使用固定秘钥对查询参数进行对称加密
        String aesEncrypt1 = AESUtil.encrypt(searchParamStr, aesEncryptKey);

        // 使用产生的随机字符串作为秘钥再次加密
        // 生成16个字符的随机字符串
        String randomStr = RandomStrUtil.create(16);
        String params = AESUtil.encrypt(aesEncrypt1, randomStr);

        // 产生的随机字符串倒序后使用RSA加密
        String randomReverse = StrUtil.reverse(randomStr);
        String encSecKey = RSAUtil.encrypt(randomReverse);

        return new FormParam(params, encSecKey);
    }
}
