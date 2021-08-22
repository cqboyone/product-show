package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.module.dao.ProductInfoDao;
import com.hikvision.productshow.module.entity.Dictionary;
import com.hikvision.productshow.module.entity.ProductFileUnique;
import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.mapstruct.ProductInfoMapping;
import com.hikvision.productshow.module.service.DictionaryService;
import com.hikvision.productshow.module.service.FileInfoService;
import com.hikvision.productshow.module.service.ProductFileUniqueService;
import com.hikvision.productshow.module.service.ProductInfoService;
import com.hikvision.productshow.module.validator.ProductInfoPageValidator;
import com.hikvision.productshow.module.vo.ProductInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_BY_ID_FILED;
import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_DATA_IS_NULL;

/**
 * 产品信息(ProductInfo)表服务实现类
 *
 * @author vv
 * @since 2021-05-19 14:54:17
 */
@Service("productInfoService")
@Slf4j
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoDao, ProductInfo> implements ProductInfoService {
    private ReentrantLock lock = new ReentrantLock();
    /**
     * mapping
     */
    @Resource
    private ProductInfoMapping productInfoMapping;

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private ProductFileUniqueService productFileUniqueService;

    @Resource
    private DictionaryService dictionaryService;

    /**
     * 根据产品id查询产品详细信息（基本信息和附件信息）
     *
     * @param productId
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ProductInfoVo findProductVoByProductId(String productId) {
        ProductInfoVo productInfoVo = Optional.ofNullable(this.findProductByProductIdAndRecordWatch(productId))
                .map(e -> this.productInfoMapping.po2Vo(e))
                .orElseThrow(() -> new ApiException(FIND_BY_ID_FILED));
        //查询关联的附件
        productInfoVo.setFiles(fileInfoService.getFileInfoVoByProductId(productId));
        //翻译字典
        translate(productInfoVo);
        return productInfoVo;
    }

    /**
     * 根据关键字模糊匹配多个字段查询
     *
     * @param v@return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    @SuppressWarnings("unchecked")
    public BasePage<ProductInfoVo> pageByParams(ProductInfoPageValidator v) {
        Page<ProductInfo> page = new Page(v.getPageNumber(), v.getPageSize());
        Page<ProductInfo> infoPage = this.page(page, Wrappers.lambdaQuery(ProductInfo.class)
                .between(v.getCreatedTimeEnd() != null && v.getCreatedTimeStart() != null,
                        ProductInfo::getCreatedTime, v.getCreatedTimeStart(), v.getCreatedTimeEnd())
                .and(StringUtils.isNotBlank(v.getKey()),
                        i -> i.like(ProductInfo::getProductArea, v.getKey())
                                .or(j -> j.like(ProductInfo::getProductIntroduction, v.getKey()))
                                .or(j -> j.like(ProductInfo::getProductLightSpot, v.getKey()))
                                .or(j -> j.like(ProductInfo::getProductName, v.getKey()))
                                .or(j -> j.like(ProductInfo::getProductLinkman, v.getKey()))
                                .or(j -> j.like(ProductInfo::getProductLinkmanTel, v.getKey()))
                )
                .eq(StringUtils.isNotBlank(v.getProductArea()), ProductInfo::getProductArea, v.getProductArea())
                .eq(StringUtils.isNotBlank(v.getProductBusiness()), ProductInfo::getProductBusiness, v.getProductBusiness())
                .orderBy(v.getIsLikeTotalAsc() != null, v.getIsLikeTotalAsc() != null ? v.getIsLikeTotalAsc() : false, ProductInfo::getLikeTotal)
                .orderBy(true, v.getIsCreatedTimeAsc() != null ? v.getIsCreatedTimeAsc() : false, ProductInfo::getCreatedTime)
        );
        List<ProductInfoVo> infoVos =
                infoPage.getRecords().stream().map(e -> this.productInfoMapping.po2Vo(e)).collect(Collectors.toList());
        //翻译字典
        infoVos.stream().forEach(e -> translate(e));
        return new BasePage(infoPage.getTotal(), infoVos);
    }

    /**
     * 删除文件信息。包括文件信息表、文件、产品信息表、产品文件关联表
     *
     * @param productIdList
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteProduct(List<String> productIdList) {
        //删除产品信息表
        this.removeByIds(productIdList);
        //查询产品文件关联表
        List<ProductFileUnique> productFileUniques = productIdList.stream()
                .map(e -> productFileUniqueService.findByProductId(e)).flatMap(e -> e.stream()).collect(Collectors.toList());
        //删除产品文件关联表
        List<String> productFileUniquesIds = productFileUniques.stream().map(ProductFileUnique::getId).collect(Collectors.toList());
        this.productFileUniqueService.removeByIds(productFileUniquesIds);
        //删除文件(包含原始文件、转码、文件信息)
        Map<String, List<String>> md5ProductIdListMap = productFileUniques.stream()
                .collect(Collectors.groupingBy(ProductFileUnique::getFileMd5, Collectors.mapping(ProductFileUnique::getProductId, Collectors.toList())));
        if (ObjectUtils.isEmpty(md5ProductIdListMap)) {
            return;
        }
        //找出可以删除的文件，即没有其他产品引用的。
        List<String> deleteMd5 = md5ProductIdListMap.entrySet().stream()
                .filter(e -> !ObjectUtils.isEmpty(e.getValue()))
                .filter(e ->
                        //校验该文件是否还有其他产品在用
                        ObjectUtils.isEmpty(this.productFileUniqueService.findProductIdsByMd5(e.getKey()))
                )
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        fileInfoService.removeByMd5s(deleteMd5);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ProductInfo findProductByProductIdAndRecordWatch(String productId) {
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                ProductInfo productInfo = Optional.ofNullable(this.getById(productId))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL));
                //这里要记录下观看记录
                productInfo.setWatchTotal(productInfo.getWatchTotal() + 1);
                this.updateById(productInfo);
                return productInfo;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("查询产品id={}失败", productId);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    public void translate(ProductInfoVo productInfoVo) {
        if (StringUtils.isNotBlank(productInfoVo.getProductArea())) {
            Dictionary productArea = dictionaryService.getById(productInfoVo.getProductArea());
            productInfoVo.setProductAreaName(productArea == null ? null : productArea.getDicValue());
        }
        if (StringUtils.isNotBlank(productInfoVo.getProductBusiness())) {
            Dictionary productBusiness = dictionaryService.getById(productInfoVo.getProductBusiness());
            productInfoVo.setProductBusinessName(productBusiness == null ? null : productBusiness.getDicValue());
        }
    }
}
