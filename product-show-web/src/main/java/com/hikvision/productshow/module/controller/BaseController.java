package com.hikvision.productshow.module.controller;

import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.threadLocal.UserHolder;

import static com.hikvision.productshow.common.enums.CommonErrorCode.PERMISSION_DENIED;

/**
 * @description:
 * @creator vv
 * @date 2021/5/19 15:44
 */
public class BaseController {

    protected void needAdmin() {
        if (!UserHolder.isAdmin()) {
            throw new ApiException(PERMISSION_DENIED);
        }
    }
}
