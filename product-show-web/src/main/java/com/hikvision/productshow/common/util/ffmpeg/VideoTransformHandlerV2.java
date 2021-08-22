package com.hikvision.productshow.common.util.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description:
 * @creator vv
 * @date 2021/6/7 14:20
 */
@Component
@Slf4j
public class VideoTransformHandlerV2 {

    private final LinkedBlockingQueue<TransformDto> todoMd5s = new LinkedBlockingQueue<>();

    private final Executor asyncServiceExecutor;

    @Autowired
    private Video2M3u8Util video2M3U8Util;

    @Resource
    private FilePathHandler filePathHandler;

    public VideoTransformHandlerV2(Executor asyncServiceExecutor) {
        this.asyncServiceExecutor = asyncServiceExecutor;
        start();
    }

    /**
     * 添加转换任务
     *
     * @param md5
     * @param md5Name
     * @param sourceRealPath
     * @return
     */
    public boolean put(String md5, String md5Name, String sourceRealPath) {
        final TransformDto transformDto = new TransformDto(md5, md5Name, sourceRealPath);
        if (todoMd5s.contains(transformDto)) {
            log.debug("任务队列中已有 {}", md5Name);
            return true;
        }
        File m3u8File = new File(filePathHandler.getM3U8FilePath(md5));
        if (m3u8File.exists()) {
            log.debug("已经存在转码文件 {}", m3u8File.getAbsolutePath());
            return true;
        }
        try {
            todoMd5s.put(transformDto);
        } catch (InterruptedException e) {
            log.error("加入转码任务 {} 失败", md5Name);
            return false;
        }
        return true;
    }

    public void start() {
        asyncServiceExecutor.execute(() -> {
            while (true) {
                try {
                    TransformDto take = todoMd5s.take();
                    if (!StringUtils.isAnyBlank(take.getMd5Name(), take.getMinIOPath(), take.getMinIOPath())) {
                        log.info("开始处理 {} 任务", take);
                        File file = new File(filePathHandler.getOutDirForName(take.getMd5()));
                        file.mkdirs();
                        String absolutePath = file.getAbsolutePath();
                        video2M3U8Util.videoConvert(take.getMinIOPath(), absolutePath, take.getMd5());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("处理任务失败");
                }
            }
        });
    }

}
