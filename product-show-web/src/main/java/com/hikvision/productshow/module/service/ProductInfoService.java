package com.hikvision.productshow.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.validator.ProductInfoPageValidator;
import com.hikvision.productshow.module.vo.ProductInfoVo;

import java.util.List;

/**
 * 产品信息(ProductInfo)表服务接口
 *
 * @author vv
 * @since 2021-05-19 14:54:17
 */
public interface ProductInfoService extends IService<ProductInfo> {

    /**
     * 根据产品id查询产品详细信息（基本信息和附件信息）
     *
     * @param productId
     */
    ProductInfoVo findProductVoByProductId(String productId);

    /**
     * 根据关键字模糊匹配多个字段查询
     *
     * @param v
     * @return
     */
    BasePage<ProductInfoVo> pageByParams(ProductInfoPageValidator v);

    /**
     * 删除文件信息。包括文件信息表、文件、产品信息表、产品文件关联表
     *
     * @param productIdList
     */
    void deleteProduct(List<String> productIdList);

}
