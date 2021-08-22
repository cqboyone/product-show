package com.hikvision.productshow.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hikvision.productshow.module.entity.ProductFileUnique;
import com.hikvision.productshow.module.validator.ProductFileUniqueInsertValidator;

import java.util.List;

/**
 * (ProductFileUnique)表服务接口
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
public interface ProductFileUniqueService extends IService<ProductFileUnique> {

    void save(String productId, List<ProductFileUniqueInsertValidator> files);

    /**
     * 根据产品id查询出该产品的产品附件关联表
     *
     * @param productId
     * @return
     */
    List<ProductFileUnique> findByProductId(String productId);

    /**
     * 根据产品id列表删除关联的文件信息
     *
     * @param productIds
     */
    void deleteByProductIds(List<String> productIds);

    List<String> findProductIdsByMd5(String md5);

    void deleteByProductIdAndMd5(String productId, String md5);
}
