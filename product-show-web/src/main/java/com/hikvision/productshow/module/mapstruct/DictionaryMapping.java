package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.Dictionary;
import com.hikvision.productshow.module.validator.DictionaryInsertValidator;
import com.hikvision.productshow.module.validator.DictionaryUpdateValidator;
import com.hikvision.productshow.module.vo.DictionaryVo;
import org.mapstruct.Mapper;

/**
 * 字典(Dictionary)表实体转换类
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@Mapper(componentModel = "spring")
public interface DictionaryMapping {

    DictionaryVo po2Vo(Dictionary po);

    Dictionary insert2Po(DictionaryInsertValidator v);

    Dictionary update2Po(DictionaryUpdateValidator v);
}
