package com.example.watermarkSpider.util.WyyAes;

import java.util.Random;

/**
 * 随机字符串生成工具
 */
public class RandomStrUtil {

    /**
     * 生成指定长度的随机字符串
     */
    public static String create(int length) {
        String[] textArray = {"a", "b", "d", "e", "f", "g", "h", "j", "m", "n", "q", "r", "t",
                "A", "B", "D", "E", "F", "G", "H", "J", "L", "M", "N", "Q", "R", "T",
                "2", "3", "4", "5", "6", "7", "8", "9"};

        int textLength = textArray.length;
        Random random = new Random();

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++) {
            int randomNum = random.nextInt(textLength);
            builder.append(textArray[randomNum]);
        }

        return builder.toString();
    }
}
