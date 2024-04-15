package com.example.watermarkSpider.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;

import static com.example.watermarkSpider.util.Const.userAgent;

public class QQMusic {

    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        try {
            String url = videoUrl;
            String sub = "https://c6.y.qq.com/base/fcgi-bin/" + url.split("https://c6.y.qq.com/base/fcgi-bin/")[1].split("@")[0];
            Connection.Response response = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute();
            String redirectUrl = response.url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window.__ssrFirstPageData__ =")) {
                    json = scriptElement.replaceAll("window.__ssrFirstPageData__ =", "").
                            replaceAll("<script>", "").replaceAll("</script>", "").replaceAll("<script crossorigin=\"true\">", "");
                    break;
                }
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            String id = jsonObject.get("vid").toString();
            String nickname = jsonObject.getJSONObject("feedInfo").getJSONObject("video").getJSONArray("singers").getJSONObject(0).get("name").toString();
            String avatar = jsonObject.getJSONObject("feedInfo").getJSONObject("video").getJSONArray("singers").getJSONObject(0).get("picurl").toString();
            String content = jsonObject.getJSONObject("feedInfo").getJSONObject("text").get("subTitle").toString();
            String title = jsonObject.getJSONObject("feedInfo").getJSONObject("text").get("title").toString();
            String time = jsonObject.getJSONObject("feedInfo").getJSONObject("text").get("publishDate").toString();
            String cover = jsonObject.getJSONObject("feedInfo").getJSONObject("video").get("first_frame_pic").toString();
            String requestStr = "{\n" +
                    "\t\"getMvUrl\": {\n" +
                    "\t\t\"module\": \"gosrf.Stream.MvUrlProxy\",\n" +
                    "\t\t\"method\": \"GetMvUrls\",\n" +
                    "\t\t\"param\": {\n" +
                    "\t\t\t\"vids\": [\"" + id + "\"],\n" +
                    "\t\t\t\"request_typet\": 10001\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "}";
            String result = HttpRequest.post("http://u.y.qq.com/cgi-bin/musicu.fcg")
                    .header("user-agent", userAgent)
                    .header("Content-Type", "application/json")
                    .header("Referer", "http://u.y.qq.com")
                    .header("Accept-Charset", "UTF-8")
                    .body(requestStr).execute().body();

            JSONObject jsonObject1 = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject1.getJSONObject("getMvUrl").getJSONObject("data").getJSONObject(id).getJSONArray("mp4");
            String dataUrl = "";
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject mp4Data = jsonArray.getJSONObject(i);
                String filetype = mp4Data.get("filetype").toString();
                if (("10".equals(filetype) || "20".equals(filetype) || "30".equals(filetype) || "40".equals(filetype) || "50".equals(filetype))
                        && !"0".equals(mp4Data.get("fileSize").toString())) {
                    dataUrl = mp4Data.getJSONArray("freeflow_url").get(0).toString();
                }
            }
            SpecificInfo specificInfo = new SpecificInfo();
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
            specificInfo.setType("qqyinyue");
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}
