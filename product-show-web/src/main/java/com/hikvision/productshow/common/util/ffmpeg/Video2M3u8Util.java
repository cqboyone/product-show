package com.hikvision.productshow.common.util.ffmpeg;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.hikvision.productshow.common.util.CommandUtils.process;
import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

/**
 * ffmpeg 4.4 版本测试通过
 */
public class Video2M3u8Util {
    private static final Logger logger = LoggerFactory.getLogger(Video2M3u8Util.class);

    /**
     * 视频编码
     */
    private static final String VIDEO_H_264 = "Video: h264";
    /**
     * 音频编码
     */
    private static final String AUDIO_AAC = "Audio: aac";
    /**
     * 进度起始字符串
     */
    private static final String START_FRAME_STR = "frame=";
    /**
     * 进度结束字符串
     */
    private static final String END_FRAME_STR = "fps";
    /**
     * 进度百分比格式化
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00%");
    /**
     * 视频分割时间
     */
    private static final String SPLIT_TIME_SECOND = "10";


    private final String ffmpegLocation;
    private final String ffprobeLocation;

    public Video2M3u8Util(String ffmpegBinDir) {
        this.ffmpegLocation = ffmpegBinDir + File.separator + "ffmpeg";
        this.ffprobeLocation = ffmpegBinDir + File.separator + "ffprobe";
    }

    /**
     * 视频文件转码成符合HLS规范的视频文件
     *
     * @param fromFile  源文件
     * @param toPath    目标文件夹
     * @param copyVideo 是否直接复制，不进行视频转码
     * @param copyAudio 是否直接复制，不进行音频转码
     * @return 转码完成的文件路径
     * @throws IOException IOException
     */
    private String videoStandardization(String fromFile, String toPath, boolean copyVideo, boolean copyAudio) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("3/4. start copy {} {} {} {}", fromFile, toPath, copyAudio, copyAudio);
        }
        String randomFileName = DigestUtils.md5DigestAsHex(fromFile.getBytes()) + ".mp4";
        List<String> command = new ArrayList<>(8);
        command.add(ffmpegLocation);
        command.add("-i");
        command.add(fromFile);
        command.add("-vcodec");
        if (copyVideo) {
            command.add("copy");
        } else {
            command.add("h264");
        }
        command.add("-acodec");
        if (copyAudio) {
            command.add("copy");
        } else {
            command.add("aac");
        }
        command.add(toPath + File.separator + randomFileName);

        process(command, null);
        if (logger.isDebugEnabled()) {
            logger.debug("4/4. end copy {} {} {} {}", fromFile, toPath, copyAudio, copyAudio);
        }
        return command.get(command.size() - 1);
    }

    /**
     * 转换视频文件为M3U8
     *
     * @param fromFile 源文件
     * @param toPath   目标路径
     * @param fileName 文件名（不要扩展名）
     */
    public void videoConvert(final String fromFile, final String toPath, final String fileName) {
        if (logger.isDebugEnabled()) {
            logger.debug("start videoConvert {} {} {}", fromFile, toPath, fileName);
        }
        try {
            Tuple2<Boolean, Boolean> compliance = checkComplianceWithSpecificationsForHls(fromFile);
            if (logger.isDebugEnabled()) {
                logger.debug("video: {} audio: {}", compliance.getT1(), compliance.getT2());
            }
            String copy = videoStandardization(fromFile, toPath, compliance.getT1(), compliance.getT2());
            // 构建命令
            List<String> command = new ArrayList<>(16);
            command.add(ffmpegLocation);
            command.add("-i");
            command.add(copy);
            command.add("-codec");
            command.add("copy");
            command.add("-vbsf");
            command.add("h264_mp4toannexb");
            command.add("-map");
            command.add("0");
            command.add("-f");
            command.add("segment");
            command.add("-segment_list");
            command.add(toPath + File.separator + fileName + ".m3u8");
            command.add("-segment_time");
            command.add(SPLIT_TIME_SECOND);
            command.add(toPath + File.separator + fileName + "-%03d.ts");
            process(command, null);
            boolean delete = new File(toPath + File.separator + DigestUtils.md5DigestAsHex(fromFile.getBytes()) + ".mp4").delete();
            if (logger.isDebugEnabled()) {
                logger.debug("delete fromFile copy file {}", delete);
                logger.debug("end videoConvert {} {} {}", fromFile, toPath, fileName);
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("video convert exception: {}", e.getMessage());
            }
        }
    }

    /**
     * 检查视频文件是否符合HLS规范
     *
     * @param wantCheckVideoFile 想要检查的视频文件
     * @return 1.视频符合？ 2.音频符合？
     * @throws IOException IOException
     */
    private Tuple2<Boolean, Boolean> checkComplianceWithSpecificationsForHls(String wantCheckVideoFile) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("1/4. start checkComplianceWithSpecificationsForHls {}", wantCheckVideoFile);
        }
        List<String> command = new ArrayList<>(3);
        command.add(ffmpegLocation);
        command.add("-i");
        command.add(wantCheckVideoFile);
        StringBuilder stringBuilder = new StringBuilder();
        process(command, stringBuilder::append);
        String s = stringBuilder.toString();
        //视频是HLS规范
        boolean video = s.contains(VIDEO_H_264);
        //音频是HLS规范
        boolean audio = s.contains(AUDIO_AAC);
        if (logger.isDebugEnabled()) {
            logger.debug("2/4. end checkComplianceWithSpecificationsForHls {}", wantCheckVideoFile);
        }
        return new Tuple2<>(video, audio);
    }

    /**
     * 获取视频帧数
     *
     * @param videoFile 视频文件
     * @return 帧数（字符串转长整形失败会返回-1）
     * @throws IOException IOException
     */
    private long getVideoFrames(String videoFile) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("start getVideoFrames");
        }
        List<String> command = new ArrayList<>(8);
        command.add(ffprobeLocation);
        command.add("-v");
        command.add("quiet");
        command.add("-print_format");
        command.add("json");
        command.add("-show_format");
        command.add("-show_streams");
        command.add(videoFile);
        StringBuilder stringBuilder = new StringBuilder();
        process(command, stringBuilder::append);
        String s = stringBuilder.toString();

        Filter videoFilter = filter(where("codec_type").is("video"));
        JSONArray read = JsonPath.read(s, "$.streams[?].nb_frames", videoFilter);
        if (logger.isDebugEnabled()) {
            logger.debug("end getVideoFrames");
        }
        if (read.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("json array is empty");
            }
            return -1;
        } else {
            return NumberUtils.toLong(read.get(0).toString(), -1);
        }
    }

    public static void main(String[] args) {
        Video2M3u8Util video2M3U8Util = new Video2M3u8Util("D:/Software/ffmpeg/ffmpeg-4.4-full_build/bin");
        //要实现创建ssss.m3u8文件
        video2M3U8Util.videoConvert("C:\\Users\\vv\\Videos\\测试2222.mp4",
                "C:\\Users\\vv\\Videos\\2\\",
                "ssss");
    }
}
