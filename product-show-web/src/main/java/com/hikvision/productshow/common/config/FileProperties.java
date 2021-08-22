package com.hikvision.productshow.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
@Data
public class FileProperties {
    /**
     * 文件输出目录
     */
    private String outDir;
    /**
     * 静态资源服务路径
     */
    private String staticRoot;
    /**
     * ffmpeg 执行文件所在目录
     */
    private String ffmpegBinDir;
    /**
     * 上传文件目录
     */
    private String fileSpace;
    /**
     * MinIO缓存空间
     */
    private String minioSpace;

}
