package com.example.watermarkSpider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.example.watermarkSpider.util.Const.userAgent;

public class Xg {

    public static void main(String[] args) {

        try {
            String sub = "https://v.douyin.com/iYxuDJTn/";
            Document document = Jsoup.connect(sub).header("user-agent", userAgent).get();
            String html = document.html();
            Document doc = Jsoup.parse(html);
            Elements scripts = doc.select("script");
            String json = "";
            for (Element element : scripts) {
                String scriptElement = element.toString();
                if (scriptElement.contains("window._SSR_DATA = ")) {
                    json = scriptElement.replaceAll("window._SSR_DATA = ", "").
                            replaceAll("<script>", "").replaceAll("</script>", "");
                    //第一个{之前的字符串替换为""
                    json = json.replaceAll("^[^{]*", "");
                    break;
                }
            }
            System.out.println(json);
            System.out.println(sub);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
