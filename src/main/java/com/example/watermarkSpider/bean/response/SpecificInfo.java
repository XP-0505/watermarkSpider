package com.example.watermarkSpider.bean.response;

import lombok.Data;

import java.util.List;

@Data
public class SpecificInfo {
    private List<String> urlList;
    private String title;
    private String address;
    private String content;
    private String username;
    private String time;
    private String type;
    private String header;

}
