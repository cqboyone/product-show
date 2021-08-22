package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 字典(Dictionary)表更新校验实体
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "DictionaryUpdateValidator", description = "字典更新校验实体")
public class DictionaryUpdateValidator extends DictionaryInsertValidator implements Serializable {
    private static final long serialVersionUID = 781978514525205248L;

    @NotBlank
    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

}
