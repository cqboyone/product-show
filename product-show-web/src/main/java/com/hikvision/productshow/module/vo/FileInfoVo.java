package com.hikvision.productshow.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息(FileInfo)表展示
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "FileInfoVo", description = "展示文件信息")
public class FileInfoVo implements Serializable {
    private static final long serialVersionUID = -34019324467240855L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "createdBy", value = "创建人")
    private String createdBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createdTime", value = "创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "updatedBy", value = "更新人")
    private String updatedBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "updatedTime", value = "更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    /**
     * md5
     */
    @ApiModelProperty(name = "fileMd5", value = "md5")
    private String fileMd5;
    /**
     * 文件真实路径
     */
    @ApiModelProperty(name = "realPath", value = "文件真实路径")
    private String realPath;
    /**
     * 文件大小
     */
    @ApiModelProperty(name = "fileSize", value = "文件大小")
    private Long fileSize;
    /**
     * 原始文件名
     */
    @ApiModelProperty(name = "beforeName", value = "原始文件名")
    private String beforeName;
    /**
     * 文件类型
     */
    @ApiModelProperty(name = "fileType", value = "文件类型")
    private String fileType;
    /**
     * 描述
     */
    @ApiModelProperty(name = "detail", value = "描述")
    private String detail;
}
