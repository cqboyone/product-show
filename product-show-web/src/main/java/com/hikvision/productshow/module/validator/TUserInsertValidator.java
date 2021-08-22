package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息(TUser)表插入校验实体
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TUserInsertValidator", description = "用户信息插入校验实体")
public class TUserInsertValidator implements Serializable {
    private static final long serialVersionUID = 739877171171533854L;

    @ApiModelProperty(name = "userName", value = "用户名", allowEmptyValue = true)
    private String userName;

    @ApiModelProperty(name = "password", value = "密码", allowEmptyValue = true)
    private String password;

    @ApiModelProperty(name = "phoneNumber", value = "电话号码", allowEmptyValue = true)
    private String phoneNumber;

}
