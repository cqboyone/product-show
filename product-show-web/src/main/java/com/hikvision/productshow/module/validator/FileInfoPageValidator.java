package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息(FileInfo)表分页查询校验实体
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "FileInfoPageValidator", description = "文件信息分页查询校验实体")
public class FileInfoPageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = 309364642680541033L;

}
