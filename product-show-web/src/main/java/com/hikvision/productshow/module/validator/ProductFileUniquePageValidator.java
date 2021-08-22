package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * (ProductFileUnique)表分页查询校验实体
 *
 * @author vv
 * @since 2021-05-21 16:27:57
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductFileUniquePageValidator", description = "分页查询校验实体")
public class ProductFileUniquePageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = 274286681743897474L;

}
