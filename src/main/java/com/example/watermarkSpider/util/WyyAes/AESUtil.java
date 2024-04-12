package com.example.watermarkSpider.util.WyyAes;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

/**
 * AES加密、解密工具
 * CBC模式
 */
public class AESUtil {

    /**
     * 偏移向量
     * 本项目中是一个固定值
     */
    private static final String iv = "0102030405060708";

    /**
     * 加密
     *
     * @param data 加密内容
     * @param key  秘钥
     */
    public static String encrypt(String data, String key) {
        byte[] ivBytes = iv.getBytes(CharsetUtil.CHARSET_UTF_8);
        byte[] dataBytes = data.getBytes(CharsetUtil.CHARSET_UTF_8);
        byte[] keyBytes = key.getBytes(CharsetUtil.CHARSET_UTF_8);

        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyBytes, ivBytes);
        return aes.encryptBase64(dataBytes);
    }

    /**
     * 解密
     *
     * @param data 要解密的数据
     * @param key  解密秘钥
     */
    public static String decrypt(String data, String key) {
        byte[] ivBytes = iv.getBytes(CharsetUtil.CHARSET_UTF_8);
        byte[] keyBytes = key.getBytes(CharsetUtil.CHARSET_UTF_8);

        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyBytes, ivBytes);
        return aes.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }
}
