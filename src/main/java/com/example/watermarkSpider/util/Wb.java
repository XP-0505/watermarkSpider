package com.example.watermarkSpider.util;

import cn.hutool.json.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.*;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Wb {

    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String sub = videoUrl;
            // 通过短连接获取长链接
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            JSONObject renderDataJson = new JSONObject();
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("$render_data")) {
                    json = scriptElement.replaceAll("<script>", "").replaceAll("</script>", "");
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("nashorn");
                    // 创建一个简单的绑定，用于存储脚本的全局变量
                    SimpleBindings bindings = new SimpleBindings();
                    // 执行JavaScript代码，并将结果存储在bindings中
                    engine.eval(json, bindings);
                    // 从bindings中获取$render_data变量
                    Map<String, Object> map = new HashMap<>(bindings);
                    Map<String, Object> renderMap = (Map<String, Object>) map.get("nashorn.global");
                    // 将结果转换为JSON对象
                    renderDataJson = new JSONObject(renderMap.get("$render_data"));
                    break;
                }
            }
            String videourl = renderDataJson.getJSONObject("status").getJSONObject("page_info").getJSONObject("media_info").get("stream_url").toString();
            try {
                String datetime = String.valueOf(System.currentTimeMillis());
                String fileName = datetime + ".mp4";
                String newVideoUrl =  DownloadUtil.download(videourl, fileName);
                specificInfo.setUrlList(new ArrayList<String>() {{
                    add(newVideoUrl);
                }});
            } catch (Exception e) {
                e.printStackTrace();
            }
            specificInfo.setType("weibo");
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
            String sub = videoUrl;
            // 通过短连接获取长链接
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            Document document = Jsoup.connect(redirectUrl).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            JSONObject renderDataJson = new JSONObject();
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("$render_data")) {
                    json = scriptElement.replaceAll("<script>", "").replaceAll("</script>", "");
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("nashorn");
                    // 创建一个简单的绑定，用于存储脚本的全局变量
                    SimpleBindings bindings = new SimpleBindings();
                    // 执行JavaScript代码，并将结果存储在bindings中
                    engine.eval(json, bindings);
                    // 从bindings中获取$render_data变量
                    Map<String, Object> map = new HashMap<>(bindings);
                    Map<String, Object> renderMap = (Map<String, Object>) map.get("nashorn.global");
                    // 将结果转换为JSON对象
                    renderDataJson = new JSONObject(renderMap.get("$render_data"));
                    break;
                }
            }
            JSONObject jsonArray = renderDataJson.getJSONObject("status").getJSONObject("pic_ids");
            List<String> imageUrlList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                String urlList = jsonArray.get(String.valueOf(i)).toString();
                urlList = "https://lz.sinaimg.cn/oslarge/" + urlList + ".jpg";
                imageUrlList.add(urlList);
            }
            specificInfo.setUrlList(imageUrlList);
            specificInfo.setType("weibo");
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}
