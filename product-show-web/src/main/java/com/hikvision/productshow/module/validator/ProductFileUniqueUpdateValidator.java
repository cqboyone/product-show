package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * (ProductFileUnique)表更新校验实体
 *
 * @author vv
 * @since 2021-05-21 16:27:57
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductFileUniqueUpdateValidator", description = "更新校验实体")
public class ProductFileUniqueUpdateValidator extends ProductFileUniqueInsertValidator implements Serializable {
    private static final long serialVersionUID = 700351694656388834L;

}
