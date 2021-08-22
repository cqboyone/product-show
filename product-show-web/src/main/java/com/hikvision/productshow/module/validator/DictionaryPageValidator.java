package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典(Dictionary)表分页查询校验实体
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "DictionaryPageValidator", description = "字典分页查询校验实体")
public class DictionaryPageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = -17768872811888279L;

    @ApiModelProperty(name = "dicType", value = "类型", allowEmptyValue = true, example = "")
    private String dicType;

    @ApiModelProperty(name = "dicParentKey", value = "父级节点key", allowEmptyValue = true, example = "")
    private String dicParentKey;

    @ApiModelProperty(name = "dicKey", value = "key", allowEmptyValue = true, example = "")
    private String dicKey;

    @ApiModelProperty(name = "dicValue", value = "value", allowEmptyValue = true, example = "")
    private String dicValue;
}
