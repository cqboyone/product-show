package com.hikvision.productshow.module.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 展示用户登录结果信息
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@Builder
@ApiModel(value = "TUserLoginVo", description = "展示用户登录结果信息")
public class TUserLoginVo implements Serializable {
    private static final long serialVersionUID = 327908029639923306L;

    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * token
     */
    @ApiModelProperty(name = "token", value = "token")
    private String token;

}
