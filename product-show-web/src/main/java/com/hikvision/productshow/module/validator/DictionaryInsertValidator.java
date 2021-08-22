package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典(Dictionary)表插入校验实体
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "DictionaryInsertValidator", description = "字典插入校验实体")
public class DictionaryInsertValidator implements Serializable {
    private static final long serialVersionUID = 695969576091647352L;

    @ApiModelProperty(name = "dicType", value = "类型", allowEmptyValue = true)
    private String dicType;

    @ApiModelProperty(name = "dicParentKey", value = "父级节点key", allowEmptyValue = true)
    private String dicParentKey;

    @ApiModelProperty(name = "dicKey", value = "key", allowEmptyValue = true)
    private String dicKey;

    @ApiModelProperty(name = "dicValue", value = "value", allowEmptyValue = true)
    private String dicValue;

}
