package com.hikvision.productshow.module.service.impl;

import com.hikvision.productshow.common.util.ffmpeg.FilePathHandler;
import com.hikvision.productshow.module.service.VideoService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private FilePathHandler filePathHandler;

    public VideoServiceImpl() {
    }

    @Override
    public void getM3u8File(String name, OutputStream outputStream) throws IOException {
        FileUtils.copyFile(new File(filePathHandler.getM3U8FilePath(name)), outputStream);
    }

    @Override
    public void getTsFile(String name, OutputStream outputStream) throws IOException {
        FileUtils.copyFile(new File(filePathHandler.getTsFilePath(name)), outputStream);
    }

}
