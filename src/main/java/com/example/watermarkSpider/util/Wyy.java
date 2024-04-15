package com.example.watermarkSpider.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import com.example.watermarkSpider.util.WyyAes.CryptoUtil;
import com.example.watermarkSpider.util.WyyAes.FormParam;
import com.example.watermarkSpider.util.WyyAes.MvParam;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Wyy {

    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String url = videoUrl;
            String sub = "http://163cn.tv/" + url.split("http://163cn.tv/")[1].split("\\(")[0];
            Connection.Response response = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute();
            String redirectUrl = response.url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.__INITIAL_PROPS__ = ")) {
                    json = scriptElement.replaceAll("window.__INITIAL_PROPS__ = ", "").
                            replaceAll("<script>", "").replaceAll("</script>", "").replaceAll(" window.ssr = true;", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String id = "";
            String dataUrl = "";
            String nickname = "";
            String avatar = "";
            String content = "";
            String title = "";
            String time = "";
            String cover = "";
            if (jsonObject.getJSONObject("mvInfo") != null) {
                id = jsonObject.getJSONObject("mvInfo").get("id").toString();
                JSONArray videoArray = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").getJSONArray("videos");
                JSONObject videoObject = videoArray.getJSONObject(videoArray.size() - 1);
                MvParam musicInfoParam = new MvParam();
                musicInfoParam.setId(id);
                musicInfoParam.setR(videoObject.get("height").toString());
                musicInfoParam.setSize(videoObject.get("size").toString());
                FormParam formParam = CryptoUtil.encrypt(musicInfoParam);
                Map<String, Object> map = new HashMap<>();
                map.put("params", formParam.getParams());
                map.put("encSecKey", formParam.getEncSecKey());
                String result = HttpRequest.post("https://music.163.com/weapi/song/enhance/play/mv/url?csrf_token=")
                        .header("user-agent", userAgent)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Referer", "https://music.163.com/mv?id=" + id)
                        .header("Accept-Charset", "UTF-8")
                        .form(map)
                        .execute().body();
                JSONObject newVideObject = JSONObject.parseObject(result);
                dataUrl = newVideObject.getJSONObject("data").get("url").toString();
                nickname = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("artistName").toString();
                avatar = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").getJSONArray("artists").getJSONObject(0).get("img1v1Url").toString();
                content = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("desc").toString();
                title = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("name").toString();
                time = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("publishTime").toString();
                cover = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("cover").toString();
            } else if (jsonObject.getJSONObject("mlogInfo") != null) {
                id = jsonObject.getJSONObject("mlogInfo").get("id").toString();
                dataUrl = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("content").getJSONObject("video").getJSONObject("urlInfo").get("url").toString();
                nickname = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("profile").get("nickname").toString();
                avatar = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("profile").get("avatarUrl").toString();
                content = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("content").get("text").toString();
                title = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("content").get("title").toString();
                cover = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").getJSONObject("content").getJSONObject("video").get("coverUrl").toString();
                time = jsonObject.getJSONObject("mlogInfo").getJSONObject("resource").get("pubTime").toString();
                //时间戳1637494341613转化为yyyy-MM-dd HH:mm:ss时间格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = sdf.format(new Date(Long.parseLong(time)));
            } else {
                return null;
            }
            specificInfo.setItemId(id);
            specificInfo.setContent(content);
            specificInfo.setUsername(nickname);
            specificInfo.setAvatar(avatar);
            specificInfo.setCover(cover);
            specificInfo.setTime(time);
            specificInfo.setTitle(title);
            String finalDataUrl = dataUrl;
            specificInfo.setUrlList(new ArrayList<String>() {{
                add(finalDataUrl);
            }});
            specificInfo.setType("wangyiyun");
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

}
