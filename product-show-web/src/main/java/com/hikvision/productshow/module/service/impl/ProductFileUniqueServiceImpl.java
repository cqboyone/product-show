package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.common.util.ForEachUtils;
import com.hikvision.productshow.module.dao.ProductFileUniqueDao;
import com.hikvision.productshow.module.entity.ProductFileUnique;
import com.hikvision.productshow.module.mapstruct.ProductFileUniqueMapping;
import com.hikvision.productshow.module.service.ProductFileUniqueService;
import com.hikvision.productshow.module.validator.ProductFileUniqueInsertValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.enums.Column.FileInfo.PRODUCT_ID;

/**
 * (ProductFileUnique)表服务实现类
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
@Service("productFileUniqueService")
public class ProductFileUniqueServiceImpl extends ServiceImpl<ProductFileUniqueDao, ProductFileUnique> implements ProductFileUniqueService {

    @Resource
    private ProductFileUniqueMapping productFileUniqueMapping;

    @Resource
    private ProductFileUniqueDao productFileUniqueDao;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void save(String productId, List<ProductFileUniqueInsertValidator> files) {
        //存储文件关联信息
        if (ObjectUtils.isEmpty(files)) {
            return;
        }
        List<ProductFileUnique> poList = files.stream()
                .map(e -> this.productFileUniqueMapping.insert2Po(e))
                .collect(Collectors.toList());
        //设置产品id
        poList.stream().forEach(e -> e.setProductId(productId));
        //排序
        ForEachUtils.forEach(0, poList, (index, e) -> e.setFileOrder(index));
        //删除该产品所有关联信息。其实应该做更新的。但是不知道为啥saveOrUpdateBatch无法保存新增加的数据
        this.deleteAllFileUniqueByProductId(productId);
        //要清除id，否则会报错。
        poList.stream().forEach(e -> e.setId(null));
        this.saveBatch(poList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<ProductFileUnique> findByProductId(String productId) {
        //查询出该产品id的关联表
        QueryWrapper<ProductFileUnique> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(PRODUCT_ID.column, productId);
        return this.list(queryWrapper);
    }

    /**
     * 根据产品id列表删除关联的文件信息
     *
     * @param productIds
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteByProductIds(List<String> productIds) {
        productIds.forEach(this::deleteAllFileUniqueByProductId);
    }

    @Override
    public List<String> findProductIdsByMd5(String md5) {
        return this.productFileUniqueDao.distinctProductIdByMd5(
                Wrappers.lambdaQuery(ProductFileUnique.class).eq(ProductFileUnique::getFileMd5, md5));
    }

    @Override
    public void deleteByProductIdAndMd5(String productId, String md5) {
        this.productFileUniqueDao.delete(Wrappers.lambdaQuery(ProductFileUnique.class)
                .eq(ProductFileUnique::getProductId, productId)
                .eq(ProductFileUnique::getFileMd5, md5));
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteAllFileUniqueByProductId(String productId) {
        List<ProductFileUnique> productFileUniques = this.list(new LambdaQueryWrapper<ProductFileUnique>()
                .eq(ProductFileUnique::getProductId, productId)
        );
        List<String> idList = productFileUniques.stream().map(ProductFileUnique::getId).collect(Collectors.toList());
        this.removeByIds(idList);
    }
}
