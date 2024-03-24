package com.example.watermarkSpider.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "VIDEO_INFO")
public class VideoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemId;

    private String videoUrl;

    private String videoName;


}
