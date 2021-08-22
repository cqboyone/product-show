package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.module.dao.TUserDao;
import com.hikvision.productshow.module.entity.TUser;
import com.hikvision.productshow.module.service.TUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息(TUser)表服务实现类
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@Service("tUserService")
public class TUserServiceImpl extends ServiceImpl<TUserDao, TUser> implements TUserService {

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public TUser findByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        return this.getOne(new LambdaQueryWrapper<TUser>().eq(TUser::getUserName, userName));
    }
}
