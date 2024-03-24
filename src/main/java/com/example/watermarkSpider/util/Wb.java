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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            List<String> videoList = new ArrayList<>();
            videoList.add(videourl);
            specificInfo.setUrlList(videoList);
            String itemId = renderDataJson.getJSONObject("status").get("id").toString();
            String content = renderDataJson.getJSONObject("status").get("text").toString();
            String time = renderDataJson.getJSONObject("status").get("created_at").toString();
            String title = renderDataJson.getJSONObject("status").get("status_title").toString();
            String username = renderDataJson.getJSONObject("status").getJSONObject("user").get("screen_name").toString();
            String avatar = renderDataJson.getJSONObject("status").getJSONObject("user").get("avatar_hd").toString();
            String cover = renderDataJson.getJSONObject("status").getJSONObject("page_info").getJSONObject("page_pic").get("url").toString();

            List<String> list = Arrays.asList(time.split(" "));
            String timeStr = list.get(5)+"-"+getMonthNumber(list.get(1))+"-"+list.get(2)+" "+list.get(3);
            specificInfo.setUsername(username);
            specificInfo.setTime(timeStr);
            specificInfo.setAvatar(DownloadUtil.loadImage(avatar));
            specificInfo.setContent(filterURLs(content));
            specificInfo.setTitle(title);
            specificInfo.setItemId(itemId);
            specificInfo.setCover(DownloadUtil.loadImage(cover));
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

            String itemId = renderDataJson.getJSONObject("status").get("id").toString();
            String content = renderDataJson.getJSONObject("status").get("text").toString();
            String time = renderDataJson.getJSONObject("status").get("created_at").toString();
            String title = renderDataJson.getJSONObject("status").get("status_title").toString();
            String username = renderDataJson.getJSONObject("status").getJSONObject("user").get("screen_name").toString();
            String avatar = renderDataJson.getJSONObject("status").getJSONObject("user").get("avatar_hd").toString();

            List<String> list = Arrays.asList(time.split(" "));
            String timeStr = list.get(5)+"-"+getMonthNumber(list.get(1))+"-"+list.get(2)+" "+list.get(3);
            specificInfo.setUsername(username);
            specificInfo.setTime(timeStr);
            specificInfo.setAvatar(DownloadUtil.loadImage(avatar));
            specificInfo.setContent(filterURLs(content));
            specificInfo.setTitle(title);
            specificInfo.setItemId(itemId);

            specificInfo.setType("weibo");
            resp.setSpecificInfo(specificInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private static String getMonthNumber(String monthAbbreviation) {
        // 创建月份缩写到数字月份的映射
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
        // 检查映射中是否存在该月份的缩写
        String monthNumber = monthMap.get(monthAbbreviation);
        if (monthNumber == null) {
            throw new IllegalArgumentException("Invalid month abbreviation: " + monthAbbreviation);
        }
        return monthNumber;
    }

    public static String filterURLs(String text) {
        // 正则表达式匹配URL，这里只是一个简单的例子，实际中可能需要更复杂的正则表达式来匹配各种URL格式
        String regex = "<a\\b(.*?)</a>";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(text).replaceAll("");
    }

    public static void main(String[] args) {
        String sub = "https://weibo.com/5288439940/5014866192502740";
        try {
            String redirectUrl = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute().url().toString();
            System.out.printf(redirectUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
