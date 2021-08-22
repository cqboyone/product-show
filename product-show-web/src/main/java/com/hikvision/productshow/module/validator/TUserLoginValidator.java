package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户信息(TUser)表插入校验实体
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TUserLoginValidator", description = "用户登录校验实体")
public class TUserLoginValidator implements Serializable {
    private static final long serialVersionUID = 739877171171533854L;

    @NotBlank
    @ApiModelProperty(name = "userName", value = "用户名", allowEmptyValue = true)
    private String userName;

    @NotBlank
    @ApiModelProperty(name = "password", value = "密码", allowEmptyValue = true)
    private String password;

}
