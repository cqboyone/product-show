package com.hikvision.productshow.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.module.entity.Dictionary;
import com.hikvision.productshow.module.validator.DictionaryPageValidator;
import com.hikvision.productshow.module.vo.DictionaryVo;

/**
 * 字典(Dictionary)表服务接口
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 分页查询数据
     *
     * @param v
     * @return
     */
    BasePage<DictionaryVo> page(DictionaryPageValidator v);

}
