package com.example.watermarkSpider.mapper;

import com.example.watermarkSpider.bean.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;

@Resource
public interface VideoInfoRepository extends JpaRepository<VideoInfo, String> {
}
