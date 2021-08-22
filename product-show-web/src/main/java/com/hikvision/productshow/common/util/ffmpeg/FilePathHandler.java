package com.hikvision.productshow.common.util.ffmpeg;

import com.hikvision.productshow.common.config.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @description:
 * @creator vv
 * @date 2021/6/9 14:09
 */
@Slf4j
@Component
public class FilePathHandler {

    @Resource
    private FileProperties fileProperties;

    /**
     * 获取m3u8转码文件路径
     */
    public String getM3U8FilePath(String name) {
        return getOutDirForName(name) + File.separator + name + ".m3u8";
    }

    /**
     * 获取ts转码文件路径
     */
    public String getTsFilePath(String name) {
        int i = name.lastIndexOf("-");
        String dir = name.substring(0, i);
        return getOutDirForName(dir) + File.separator + name + ".ts";
    }

    /**
     * 文件输出目录拼接md5文件夹路径：xxx/name
     */
    public String getOutDirForName(String name) {
        return fileProperties.getOutDir() + File.separator + name;
    }

    /**
     * 获取文件在MinIO的真实路径（仅适用于MinIO和服务在同一台服务器）
     */
    public String getFileRealPathInMinIO(String type, String fileName) {
        return fileProperties.getMinioSpace() + File.separator + type + File.separator + fileName;
    }
}
