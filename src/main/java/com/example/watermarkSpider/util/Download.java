package com.example.watermarkSpider.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Download {
    public static boolean download(String url, String fileName){
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 设置请求头
            connection.setRequestProperty("User-Agent", userAgent);
            //微博下载视频需要携带下列请求头
//            connection.setRequestProperty("Referer",
//                    "https://m.weibo.cn/");
//            connection.setRequestProperty("Sec-Fetch-Dest",
//                    "video");
//            connection.setRequestProperty("Sec-Fetch-Mode",
//                    "no-cors");
//            connection.setRequestProperty("Sec-Fetch-Site",
//                    "cross-site");
            // 连接并获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 获取输入流并读取数据
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                // 写入到本地文件
                try (OutputStream outputStream = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                // 关闭流
                inputStream.close();
            } else {
                System.out.println("Script element not found");
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String fileName = "D:\\" + System.currentTimeMillis() + ".mp4";
        download("https://f.video.weibocdn.com/u0/sn4TyTdigx08dnLYzpuU01041200cM550E010.mp4?label=mp4_hd&template=540x960.24.0&ori=0&ps=1BThihd3VLAY5R&Expires=1710835509&ssig=%2BMqlGJKeRi&KID=unistore,video",fileName);
    }
}
