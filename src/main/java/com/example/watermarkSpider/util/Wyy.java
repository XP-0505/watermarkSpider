package com.example.watermarkSpider.util;

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

import java.util.ArrayList;
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

            String id = jsonObject.getJSONObject("mvInfo").get("id").toString();

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
            String dataUrl = newVideObject.getJSONObject("data").get("url").toString();

            String nickname = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("artistName").toString();
            String avatar = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").getJSONArray("artists").getJSONObject(0).get("img1v1Url").toString();
            String content = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("desc").toString();
            String title = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("name").toString();
            String time = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("publishTime").toString();
            String cover = jsonObject.getJSONObject("mvInfo").getJSONObject("resource").getJSONObject("data").get("cover").toString();

            specificInfo.setItemId(id);
            specificInfo.setContent(content);
            specificInfo.setUsername(nickname);
            specificInfo.setAvatar(avatar);
            specificInfo.setCover(cover);
            specificInfo.setTitle(time);
            specificInfo.setTitle(title);
            specificInfo.setUrlList(new ArrayList<String>() {{
                add(dataUrl);
            }});
            specificInfo.setType("wangyiyun");
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

}
