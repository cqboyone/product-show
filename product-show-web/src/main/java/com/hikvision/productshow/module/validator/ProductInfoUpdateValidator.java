package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 产品信息(ProductInfo)表更新校验实体
 *
 * @author vv
 * @since 2021-05-20 16:37:11
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductInfoUpdateValidator", description = "产品信息更新校验实体")
public class ProductInfoUpdateValidator extends ProductInfoInsertValidator implements Serializable {
    private static final long serialVersionUID = -63714862366285647L;

    @NotBlank
    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

}
