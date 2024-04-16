package com.example.watermarkSpider.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ZuiYou {

    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String redirectUrl = videoUrl.substring(videoUrl.indexOf("https"));
            Document document = Jsoup.connect(redirectUrl).maxBodySize(0).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = new String();
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.APP_INITIAL_STATE=")) {
                    json = scriptElement.replaceAll("window.APP_INITIAL_STATE=", "").
                            replaceAll("<script>", "").replaceAll("</script>", "").replaceAll("<script id=\"appState\">", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String id = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONArray("imgs").getJSONObject(0).get("id").toString();
            String video = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("videos").getJSONObject(id).get("urlwm").toString();
            specificInfo.setType("zuiyou");
            specificInfo.setItemId(id);
            String nickname = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("member").get("name").toString();
            String avatar = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("member").getJSONObject("avatarUrls").
                    getJSONObject("origin").getJSONArray("urls").get(0).toString();
            String content = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("content").toString();
            String time = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("ct").toString();
            String title = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("content").toString();
            String cover = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("videos").getJSONObject(id).getJSONArray("coverUrls").get(0).toString();
            Date date = new Date(Long.valueOf(time) * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            specificInfo.setUrlList(Collections.singletonList(video));
            specificInfo.setCover(cover);
            specificInfo.setUsername(nickname);
            specificInfo.setAvatar(avatar);
            specificInfo.setContent(content);
            specificInfo.setTime(timeStr);
            specificInfo.setTitle(title);
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
            String redirectUrl = videoUrl.substring(videoUrl.indexOf("https"));
            Document document = Jsoup.connect(redirectUrl).maxBodySize(0).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = new String();
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.APP_INITIAL_STATE=")) {
                    json = scriptElement.replaceAll("window.APP_INITIAL_STATE=", "").
                            replaceAll("<script>", "").replaceAll("</script>", "").replaceAll("<script id=\"appState\">", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String id = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").getJSONObject("post").get("id").toString();
            JSONArray imageArray = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").getJSONObject("post").getJSONArray("imgs");
            List<String> imageUrlList = new ArrayList<>();
            for (int i = 0; i < imageArray.size(); i++) {
                String imageUrl = imageArray.getJSONObject(i).getJSONObject("urls").getJSONObject("origin").getJSONArray("urls").get(0).toString();
                imageUrlList.add(imageUrl);
            }
            specificInfo.setUrlList(imageUrlList);
            specificInfo.setType("zuiyou");
            specificInfo.setItemId(id);
            String nickname = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("member").get("name").toString();
            String avatar = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").getJSONObject("member").getJSONObject("avatarUrls").
                    getJSONObject("origin").getJSONArray("urls").get(0).toString();
            String content = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("content").toString();
            String time = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("ct").toString();
            String title = jsonObject.getJSONObject("sharePost").getJSONObject("postDetail").
                    getJSONObject("post").get("content").toString();
            Date date = new Date(Long.valueOf(time) * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            specificInfo.setUsername(nickname);
            specificInfo.setAvatar(avatar);
            specificInfo.setContent(content);
            specificInfo.setTime(timeStr);
            specificInfo.setTitle(title);
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

}
