package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息(TUser)表分页查询校验实体
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TUserPageValidator", description = "用户信息分页查询校验实体")
public class TUserPageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = 407464948413388495L;

}
