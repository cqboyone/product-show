package com.hikvision.productshow.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息(FileInfo)表实体类
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@SuppressWarnings("serial")
@Data
public class FileInfo extends Model<FileInfo> {
    /**
     * 主键
     */
    private String id;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;
    /**
     * md5
     */
    private String fileMd5;
    /**
     * 文件真实路径
     */
    private String realPath;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 原始文件名
     */
    private String beforeName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 新文件名
     */
    private String newName;
    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
