package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 文件信息(FileInfo)表更新校验实体
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "FileInfoUpdateValidator", description = "文件信息更新校验实体")
public class FileInfoUpdateValidator extends FileInfoInsertValidator implements Serializable {
    private static final long serialVersionUID = -14880815052034417L;

    @NotBlank
    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

}
