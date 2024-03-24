package com.example.watermarkSpider.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.watermarkSpider.bean.response.BaseResp;
import com.example.watermarkSpider.bean.response.SpecificInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.watermarkSpider.util.Const.userAgent;

public class KS {
    public static BaseResp video(String videoUrl) {
        BaseResp resp = new BaseResp();
        SpecificInfo specificInfo = new SpecificInfo();
        try {
            String url = videoUrl;
            String sub = "https://v.kuaishou.com/" + url.split("https://v.kuaishou.com/")[1].split("/")[0];
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
            String sub = "https://v.kuaishou.com/" + url.split("https://v.kuaishou.com/")[1].split("/")[0];
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

    public static void main(String[] args) {
        try {
            String sub = "https://v.kuaishou.com/sDZeGv";
            Connection.Response response = Jsoup.connect(sub).header("user-agent", userAgent).followRedirects(true).execute();
            String redirectUrl = response.url().toString();
            Map<String, String> cookies = null;
            cookies=response.cookies();
            Map<String, String> videoMap = extractQueryParams(redirectUrl);
            String fid = videoMap.get("fid");
            String shareToken = videoMap.get("shareToken");
            String shareObjectId = videoMap.get("shareObjectId");
            String kpn = videoMap.get("kpn");
            String photoId = videoMap.get("photoId");
            String shareId = videoMap.get("shareId");


            Map<String, String> map = new HashMap<>();
            map.put("fid", fid);
            map.put("shareToken", shareToken);
            map.put("shareObjectId", shareObjectId);
            map.put("shareMethod", "TOKEN");
            map.put("shareId", shareId);
            map.put("shareResourceType", "PHOTO_OTHER");
            map.put("shareChannel", "share_copylink");
            map.put("kpn", kpn);
            map.put("subBiz", "BROWSE_SLIDE_PHOTO");
            map.put("env", "SHARE_VIEWER_ENV_TX_TRICK");
            map.put("h5Domain", "v.m.chenzhongtech.com");
            map.put("photoId", photoId);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(map);

            //cookies 转成;分割的字符串
            String cookieString = "";
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookieString += entry.getKey() + "=" + entry.getValue() + ";";
            }

            String result = HttpRequest.post("https://v.m.chenzhongtech.com/rest/wd/photo/info?kpn=KUAISHOU&captchaToken=")
                    .header("user-agent", userAgent).header("Referer", redirectUrl)
                    .header("Content-Type","application/json")
                    .header("Connection","keep-alive")
                    .header("Accept","application/json, text/plain, */*")
                    .header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                    .header("Accept-Encoding","gzip, deflate, br")
                    .header("Host","v.m.chenzhongtech.com")
                    .header("Origin","https://v.kuaishou.com")
                    .header("Sec-Fetch-Dest","empty")
                    .header("Sec-Fetch-Mode","cors")
                    .header("Sec-Fetch-Site","same-site")
                    .header("Referer","https://v.kuaishou.com/tOggGY")
                    .header("sec-ch-ua","\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102\"")
                    .header("sec-ch-ua-mobile","?0")
                    .header("sec-ch-ua-platform","\"Windows\"")
                    .header("Connection","keep-alive")
                    .header("Content-Length","0")
                    .header("TE","trailers")
                    .header("Cookie",cookieString)
                    .body(json).execute().body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            String videoUrl = jsonObject.get("mp4Url").toString();

            String userName = jsonObject.getJSONObject("photo").get("userName").toString();
            String avatar = jsonObject.getJSONObject("photos").get("headUrl").toString();
            String time = jsonObject.getJSONObject("photo").get("time").toString();
            String content = jsonObject.getJSONObject("photo").get("caption").toString();
            String cover = jsonObject.getJSONObject("photo").getJSONArray("coverUrls").getJSONObject(0).get("url").toString();
            Date date = new Date(Long.valueOf(time));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            SpecificInfo specificInfo = new SpecificInfo();
            specificInfo.setCover(cover);
            specificInfo.setAvatar(avatar);
            specificInfo.setContent(content);
            specificInfo.setTime(timeStr);
            specificInfo.setUsername(userName);
            specificInfo.setItemId(fid);
            specificInfo.setUrlList(Collections.singletonList(videoUrl));


            System.out.println(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> extractQueryParams(String url) throws Exception {
        URL parsedURL = new URL(url);
        Map<String, String> queryPairs = new HashMap<>();
        String query = parsedURL.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return queryPairs;
    }
}
