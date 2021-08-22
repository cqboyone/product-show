package com.hikvision.productshow.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息(TUser)表展示
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TUserVo", description = "展示用户信息")
public class TUserVo implements Serializable {
    private static final long serialVersionUID = 327908029639923306L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "createdBy", value = "创建人")
    private String createdBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createdTime", value = "创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "updatedBy", value = "更新人")
    private String updatedBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "updatedTime", value = "更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码")
    private String password;
    /**
     * 电话号码
     */
    @ApiModelProperty(name = "phoneNumber", value = "电话号码")
    private String phoneNumber;
}
