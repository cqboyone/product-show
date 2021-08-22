package com.hikvision.productshow.module.controller;

import com.hikvision.productshow.module.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @creator vv
 * @date 2021/5/18 16:34
 */
@Controller
@Slf4j
@Api(tags = "视频模块")
//@Validated //要验证list需要使用该注解
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 请求m3u8
     *
     * @param name     文件名
     * @param response {@link HttpServletResponse}
     * @throws IOException IOException
     */
    @ApiOperation(value = "获取m3u8文件", notes = "获取m3u8文件")
    @GetMapping("/hls/{name}.m3u8")
    public void m3u8(@PathVariable String name, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("request {}.m3u8", name);
        }
        videoService.getM3u8File(name, response.getOutputStream());
    }

    /**
     * 请求ts
     *
     * @param name     文件名
     * @param index    索引
     * @param response {@link HttpServletResponse}
     * @throws IOException IOException
     */
    @ApiOperation(value = "请求ts", notes = "请求ts")
    @GetMapping("/hls/{name}-{index}.ts")
    public void ts(@PathVariable String name, @PathVariable String index, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("request {}-{}.ts", name, index);
        }
        videoService.getTsFile(name + "-" + index, response.getOutputStream());
    }
}
