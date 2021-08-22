package com.hikvision.productshow.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hikvision.productshow.module.entity.TUser;

/**
 * 用户信息(TUser)表服务接口
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
public interface TUserService extends IService<TUser> {

    TUser findByUserName(String userName);
}
