package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.ProductFileUnique;
import com.hikvision.productshow.module.validator.ProductFileUniqueInsertValidator;
import com.hikvision.productshow.module.validator.ProductFileUniqueUpdateValidator;
import com.hikvision.productshow.module.vo.ProductFileUniqueVo;
import org.mapstruct.Mapper;

/**
 * (ProductFileUnique)表实体转换类
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
@Mapper(componentModel = "spring")
public interface ProductFileUniqueMapping {

    ProductFileUniqueVo po2Vo(ProductFileUnique po);

    ProductFileUnique insert2Po(ProductFileUniqueInsertValidator v);

    ProductFileUnique update2Po(ProductFileUniqueUpdateValidator v);
}
