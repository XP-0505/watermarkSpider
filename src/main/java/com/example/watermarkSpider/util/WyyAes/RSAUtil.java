package com.example.watermarkSpider.util.WyyAes;

import java.math.BigInteger;

/**
 * RSA加密工具类
 */
public class RSAUtil {

    private static final String modStr = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
    private static final String pubKeyStr = "10001";

    public static String encrypt(String data) {
        BigInteger dataBi = new BigInteger(strToHex(data), 16);
        BigInteger modBi = new BigInteger(modStr, 16);
        BigInteger pubKeyBi = new BigInteger(pubKeyStr, 16);

        BigInteger resBi = dataBi.modPow(pubKeyBi, modBi);

        return zeroFill(resBi.toString(16), 256);
    }

    /**
     * 字符串转成16进制字符串
     */
    private static String strToHex(String data) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            int ch = data.charAt(i);
            String tmp = Integer.toHexString(ch);
            sb.append(tmp);
        }

        return sb.toString();
    }

    /**
     * 长度不够前面补充0
     */
    private static String zeroFill(String str, int size) {
        StringBuilder strBuilder = new StringBuilder(str);

        while (strBuilder.length() < size) {
            strBuilder.insert(0, "0");
        }

        str = strBuilder.toString();

        return str;
    }
}
