package com.hikvision.productshow.common.config;

import com.hikvision.productshow.common.util.ffmpeg.Video2M3u8Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @description:
 * @creator vv
 * @date 2021/5/18 16:31
 */
@Configuration
@Slf4j
public class VideoConfig {

    @Autowired
    private FileProperties fileProperties;

    @Bean
    public Video2M3u8Util video2M3u8Helper() {
        return new Video2M3u8Util(fileProperties.getFfmpegBinDir());
    }

    /**
     * 自定义临时文件目录，确保目录不会被系统删除导致抛异常
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = fileProperties.getFileSpace();
        log.info("location={}", location);
        File tmpDir = new File(location);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        factory.setLocation(tmpDir.getAbsolutePath());
        return factory.createMultipartConfig();
    }
}
