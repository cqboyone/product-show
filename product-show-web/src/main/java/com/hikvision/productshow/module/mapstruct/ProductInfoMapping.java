package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.validator.ProductInfoInsertValidator;
import com.hikvision.productshow.module.validator.ProductInfoUpdateValidator;
import com.hikvision.productshow.module.vo.ProductInfoVo;
import org.mapstruct.Mapper;

/**
 * 产品信息(ProductInfo)表实体转换类
 *
 * @author vv
 * @since 2021-05-20 15:29:43
 */
@Mapper(componentModel = "spring")
public interface ProductInfoMapping {

    ProductInfoVo po2Vo(ProductInfo po);

    ProductInfo insert2Po(ProductInfoInsertValidator v);

    ProductInfo update2Po(ProductInfoUpdateValidator v);
}
