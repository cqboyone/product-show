package com.hikvision.productshow.common.util;

import com.hikvision.productshow.common.enums.CommonErrorCode;
import com.hikvision.productshow.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description:
 * @creator vv
 * @date 2021/5/18 9:44
 */
@Slf4j
public class FileUtil {

    /**
     * 获取上传文件的md5
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMd5(MultipartFile file) {
        if (file == null) {
            throw new ApiException(CommonErrorCode.FILE_NOT_NULL);
        }
        try {
            byte[] uploadBytes = file.getBytes();
            //file->byte[],生成md5
            String md5Hex = DigestUtils.md5DigestAsHex(uploadBytes);
            //file->InputStream,生成md5
//            String md5Hex1 = DigestUtils.md5DigestAsHex(file.getInputStream());
            return md5Hex;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取文件格式
     *
     * @param file
     * @return
     */
    public static String getSuffix(MultipartFile file) {
        if (file == null) {
            throw new ApiException(CommonErrorCode.FILE_NOT_NULL);
        }
        String fileName = file.getOriginalFilename();
        return getSuffix(fileName);
    }

    /**
     * 获取文件格式
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new ApiException(CommonErrorCode.FILE_NAME_IS_NULL);
        }
        // 文件重命名  1.png -> ["1", "png"]
        String fileNameArr[] = fileName.split("\\.");
        // 获取文件的后缀名
        return fileNameArr[fileNameArr.length - 1];
    }

    /**
     * 重命名文件。命名为md5.扩展名
     *
     * @param file
     * @return
     */
    public static String renameFileAsMd5(MultipartFile file) {
        return getMd5(file) + "." + getSuffix(file);
    }
}
