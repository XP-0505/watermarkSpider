package com.example.watermarkSpider.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Dy {
    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String url = videoUrl;
            String sub = "https://v.douyin.com/" + url.split("https://v.douyin.com/")[1].split("/")[0] + "/";
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).header("user-agent", userAgent).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Element scriptElement = doc.getElementById("RENDER_DATA");
            if (scriptElement != null) {
                // 获取 script 标签内的内容
                String scriptContent = scriptElement.html();
                String substring = URLDecoder.decode(scriptContent, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(substring);
                //视频地址
                String play_url = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").get(0).toString();
                play_url = play_url.replaceAll("playwm", "play");
                String finalPlay_url = play_url;
                //其他信息
                String itemId = jsonObject.getJSONObject("app").get("itemId").toString();
                String content = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).get("desc").toString();
                String username = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("author").get("nickname").toString();
                String avatar = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("author").getJSONObject("avatar_thumb").getJSONArray("url_list").get(0).toString();
                String cover = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("cover").getJSONArray("url_list").get(0).toString();
                specificInfo.setItemId(itemId);
                specificInfo.setContent(content);
                specificInfo.setUsername(username);
                specificInfo.setAvatar(avatar);
                specificInfo.setCover(cover);
                specificInfo.setUrlList(new ArrayList<String>() {{
                    add(finalPlay_url);
                }});
                specificInfo.setType("douyin");
            }
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static BaseResp image(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String url = videoUrl;
            String sub = "https://v.douyin.com/" + url.split("https://v.douyin.com/")[1].split("/")[0] + "/";
            // 通过短连接获取长链接
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).header("user-agent", userAgent).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Element scriptElement = doc.getElementById("RENDER_DATA");
            if (scriptElement != null) {
                // 获取 script 标签内的内容
                String scriptContent = scriptElement.html();
                String substring = URLDecoder.decode(scriptContent, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(substring);
                JSONArray jsonArray = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONArray("images");
                List<String> imageUrlList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    String urlList = jsonArray.getJSONObject(i).getJSONArray("url_list").get(0).toString();
                    imageUrlList.add(urlList);
                }
                specificInfo.setUrlList(imageUrlList);
                String itemId = jsonObject.getJSONObject("app").get("itemId").toString();
                String content = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).get("desc").toString();
                String username = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("author").get("nickname").toString();
                String avatar = jsonObject.getJSONObject("app").getJSONObject("videoInfoRes").getJSONArray("item_list").getJSONObject(0).getJSONObject("author").getJSONObject("avatar_thumb").getJSONArray("url_list").get(0).toString();
                specificInfo.setItemId(itemId);
                specificInfo.setContent(content);
                specificInfo.setUsername(username);
                specificInfo.setAvatar(avatar);

                specificInfo.setType("douyin");
            }
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}
