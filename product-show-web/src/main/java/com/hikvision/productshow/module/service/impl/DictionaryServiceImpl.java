package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.module.dao.DictionaryDao;
import com.hikvision.productshow.module.entity.Dictionary;
import com.hikvision.productshow.module.mapstruct.DictionaryMapping;
import com.hikvision.productshow.module.service.DictionaryService;
import com.hikvision.productshow.module.validator.DictionaryPageValidator;
import com.hikvision.productshow.module.vo.DictionaryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 字典(Dictionary)表服务实现类
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends ServiceImpl<DictionaryDao, Dictionary> implements DictionaryService {

    /**
     * mapping
     */
    @Resource
    private DictionaryMapping dictionaryMapping;

    /**
     * 分页查询数据
     *
     * @param v
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public BasePage<DictionaryVo> page(DictionaryPageValidator v) {
        Page<Dictionary> page = new Page(v.getPageNumber(), v.getPageSize());
        Page<Dictionary> infoPage = this.page(page, Wrappers.lambdaQuery(Dictionary.class)
                .eq(StringUtils.isNotBlank(v.getDicKey()), Dictionary::getDicKey, v.getDicKey())
                .eq(StringUtils.isNotBlank(v.getDicType()), Dictionary::getDicType, v.getDicType())
                .eq(StringUtils.isNotBlank(v.getDicParentKey()), Dictionary::getDicParentKey, v.getDicParentKey())
                .like(StringUtils.isNotBlank(v.getDicValue()), Dictionary::getDicValue, v.getDicValue())
        );
        BasePage<DictionaryVo> basePage = new BasePage(infoPage.getTotal(),
                infoPage.getRecords().stream().map(e -> this.dictionaryMapping.po2Vo(e)).collect(Collectors.toList()));
        return basePage;
    }
}
