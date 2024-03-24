package com.example.watermarkSpider.util;

import cn.hutool.core.date.DateTime;
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
import java.util.Date;
import java.util.List;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Xhs {

    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String url = videoUrl;
            String sub = "http://xhslink.com/" + url.split("http://xhslink.com/")[1].split("，")[0];
            // 通过短连接获取长链接
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.__INITIAL_STATE__=")) {
                    json = scriptElement.replaceAll("window.__INITIAL_STATE__=", "").replaceAll("<script>", "").replaceAll("</script>", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String firstNoteId = jsonObject.getJSONObject("note").get("firstNoteId").toString();
            String video = "";
            try {
                video = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId)
                        .getJSONObject("note").getJSONObject("video").getJSONObject("media").getJSONObject("stream").getJSONArray("h264").getJSONObject(0).get("masterUrl").toString();
            } catch (Exception e) {
                video = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId)
                        .getJSONObject("note").getJSONObject("video").getJSONObject("media").getJSONObject("stream").getJSONArray("h265").getJSONObject(0).get("masterUrl").toString();
            }
            String finalVideoUrl = video;
            specificInfo.setUrlList(new ArrayList<String>() {{
                add(finalVideoUrl);
            }});
            specificInfo.setItemId(firstNoteId);
            String nickname = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONObject("user").get("nickname").toString();
            String avatar = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONObject("user").get("avatar").toString();
            String content = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("desc").toString();
            String title = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("title").toString();
            String time = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("time").toString();
            String cover = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONArray("imageList").getJSONObject(0).get("urlDefault").toString();
            Date date = new Date(Long.valueOf(time));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            specificInfo.setUsername(nickname);
            specificInfo.setAvatar(avatar);
            specificInfo.setContent(content);
            specificInfo.setTime(timeStr);
            specificInfo.setTitle(title);
            specificInfo.setType("xiaohongshu");
            specificInfo.setCover(cover);
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
            String sub = "http://xhslink.com/" + url.split("http://xhslink.com/")[1].split("，")[0];
            // 通过短连接获取长链接
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.__INITIAL_STATE__=")) {
                    json = scriptElement.replaceAll("window.__INITIAL_STATE__=", "").replaceAll("<script>", "").replaceAll("</script>", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String firstNoteId = jsonObject.getJSONObject("note").get("firstNoteId").toString();
            JSONArray jsonArray = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONArray("imageList");
            List<String> imageUrlList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                String urlList = jsonArray.getJSONObject(i).get("urlDefault").toString();
                imageUrlList.add(urlList);
            }
            specificInfo.setUrlList(imageUrlList);
            specificInfo.setType("xiaohongshu");
            specificInfo.setItemId(firstNoteId);
            String nickname = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONObject("user").get("nickname").toString();
            String avatar = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").getJSONObject("user").get("avatar").toString();
            String content = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("desc").toString();
            String time = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("time").toString();
            String title = jsonObject.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note").get("title").toString();
            Date date = new Date(Long.valueOf(time));
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

    public static void main(String[] args) {
        try {
            String sub = "http://xhslink.com/9ysxuE";
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            System.out.printf(redirectUrl);
        }catch (Exception e){

        }
    }

}
