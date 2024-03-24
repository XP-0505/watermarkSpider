package com.example.watermarkSpider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static com.example.watermarkSpider.util.Const.filePath;
import static com.example.watermarkSpider.util.Const.userAgent;


@Component
public class DownloadUtil {

    private static Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    public static String download(String url, String type, String fileName) {
        String videoUrl = "";
        try {
            String newFilePath = filePath;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                newFilePath = "D:" + newFilePath;
            }
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 设置请求头
            connection.setRequestProperty("User-Agent", userAgent);
            //微博下载视频需要携带下列请求头
            if ("weibo".equals(type)) {
                connection.setRequestProperty("Referer",
                        "https://m.weibo.cn/");
            }
            connection.setRequestProperty("Sec-Fetch-Dest",
                    "video");
            connection.setRequestProperty("Sec-Fetch-Mode",
                    "no-cors");
            connection.setRequestProperty("Sec-Fetch-Site",
                    "cross-site");
            String newFileName = newFilePath + fileName;
            File file = new File(newFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 连接并获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 获取输入流并读取数据
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                // 写入到本地文件
                try (OutputStream outputStream = new FileOutputStream(newFileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                // 关闭流
                inputStream.close();
            } else {
                logger.info("未找到资源，下载失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoUrl;
    }

    public static String loadImage(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 设置请求头
            connection.setRequestProperty("User-Agent", userAgent);
            //微博下载视频需要携带下列请求头
            connection.setRequestProperty("Referer",
                    "https://m.weibo.cn/");
            connection.setRequestProperty("Sec-Fetch-Dest",
                    "video");
            connection.setRequestProperty("Sec-Fetch-Mode",
                    "no-cors");
            connection.setRequestProperty("Sec-Fetch-Site",
                    "cross-site");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 关闭流
                InputStream inStream = connection.getInputStream();
                int len = -1;
                ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                while ((len = inStream.read(data)) != -1) {
                    outPut.write(data, 0, len);
                }
                inStream.close();
                byte[] encode = Base64.getEncoder().encode(outPut.toByteArray());
                String res = new String(encode);
                return "data:image/jpeg;base64," + res;
            } else {
                logger.info("未找到资源，下载失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    }
}
