package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息(FileInfo)表插入校验实体
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "FileInfoInsertValidator", description = "文件信息插入校验实体")
public class FileInfoInsertValidator implements Serializable {
    private static final long serialVersionUID = -88582922980508998L;

    @ApiModelProperty(name = "fileMd5", value = "md5", allowEmptyValue = true)
    private String fileMd5;

    @ApiModelProperty(name = "realPath", value = "文件真实路径", allowEmptyValue = true)
    private String realPath;

    @ApiModelProperty(name = "fileSize", value = "文件大小", allowEmptyValue = true)
    private Long fileSize;

    @ApiModelProperty(name = "beforeName", value = "原始文件名", allowEmptyValue = true)
    private String beforeName;

    @ApiModelProperty(name = "fileType", value = "文件类型", allowEmptyValue = true)
    private String fileType;

}
