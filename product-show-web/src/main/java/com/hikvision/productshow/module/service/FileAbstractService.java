package com.hikvision.productshow.module.service;

import com.hikvision.productshow.common.enums.CommonErrorCode;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.module.entity.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.hikvision.productshow.common.constants.FileConstants.FILE_TYPES;
import static com.hikvision.productshow.common.constants.FileConstants.FILE_TYPE_OTHER;
import static com.hikvision.productshow.common.constants.FileConstants.FILE_TYPE_PHOTO;
import static com.hikvision.productshow.common.constants.FileConstants.FILE_TYPE_VIDEO;
import static com.hikvision.productshow.common.constants.FileConstants.PHOTO_TYPES;
import static com.hikvision.productshow.common.constants.FileConstants.VIDEO_TYPES;

/**
 * @description:
 * @creator vv
 * @date 2021/6/4 16:31
 */
public abstract class FileAbstractService {

    public abstract String uploadFile(String type, MultipartFile file);
    public abstract void downloadFile(String md5, HttpServletResponse response);

    public void checkFileType(String fileType, String suffix) {
        switch (fileType) {
            case FILE_TYPE_VIDEO:
                if (!Arrays.stream(VIDEO_TYPES).anyMatch(type -> type.equalsIgnoreCase(suffix))) {
                    throw new ApiException(CommonErrorCode.FILE_FORMAT_ILLEGAL);
                }
                break;
            case FILE_TYPE_PHOTO:
                if (!Arrays.stream(PHOTO_TYPES).anyMatch(type -> type.equalsIgnoreCase(suffix))) {
                    throw new ApiException(CommonErrorCode.PHOTO_FORMAT_ILLEGAL);
                }
                break;
            case FILE_TYPE_OTHER:
                break;
            default:
                throw new ApiException(CommonErrorCode.FILE_TYPE_ILLEGAL);
        }
    }

    /**
     * 校验类型是否合法
     * @param fileType
     */
    public void checkFileType(String fileType) {
        boolean b = Arrays.stream(FILE_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileType));
        if (!b) {
            throw new ApiException(CommonErrorCode.FILE_TYPE_ILLEGAL);
        }
    }

    protected String getBeforeName(FileInfo fileInfo) {
        String beforeName = fileInfo.getBeforeName();
        if (StringUtils.isNotBlank(beforeName)) {
            return beforeName;
        }
        return null;
    }
}
