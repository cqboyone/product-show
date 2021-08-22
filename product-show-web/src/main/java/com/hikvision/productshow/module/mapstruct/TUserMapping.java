package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.TUser;
import com.hikvision.productshow.module.validator.TUserInsertValidator;
import com.hikvision.productshow.module.validator.TUserUpdateValidator;
import com.hikvision.productshow.module.vo.TUserVo;
import org.mapstruct.Mapper;

/**
 * 用户信息(TUser)表实体转换类
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@Mapper(componentModel = "spring")
public interface TUserMapping {

    TUserVo po2Vo(TUser po);

    TUser insert2Po(TUserInsertValidator v);

    TUser update2Po(TUserUpdateValidator v);
}
