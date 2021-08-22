package com.hikvision.productshow.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 产品信息(ProductInfo)表展示
 *
 * @author vv
 * @since 2021-06-04 09:50:51
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductInfoVo", description = "展示产品信息")
public class ProductInfoVo implements Serializable {
    private static final long serialVersionUID = 262795353707153705L;
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
     * 产品名称
     */
    @ApiModelProperty(name = "productName", value = "产品名称")
    private String productName;
    /**
     * 所属行业
     */
    @ApiModelProperty(name = "productBusiness", value = "所属行业")
    private String productBusiness;
    /**
     * 所属行业中文
     */
    @ApiModelProperty(name = "productBusinessName", value = "所属行业中文")
    private String productBusinessName;
    /**
     * 所属区域
     */
    @ApiModelProperty(name = "productArea", value = "所属区域")
    private String productArea;
    /**
     * 所属区域中文
     */
    @ApiModelProperty(name = "productAreaName", value = "所属区域中文")
    private String productAreaName;
    /**
     * 产品联系人电话
     */
    @ApiModelProperty(name = "productLinkmanTel", value = "产品联系人电话")
    private String productLinkmanTel;
    /**
     * 产品联系人
     */
    @ApiModelProperty(name = "productLinkman", value = "产品联系人")
    private String productLinkman;
    /**
     * 产品简介
     */
    @ApiModelProperty(name = "productIntroduction", value = "产品简介")
    private String productIntroduction;
    /**
     * 产品亮点
     */
    @ApiModelProperty(name = "productLightSpot", value = "产品亮点")
    private String productLightSpot;
    /**
     * 点赞
     */
    @ApiModelProperty(name = "likeTotal", value = "点赞")
    private Integer likeTotal;
    /**
     * 观看
     */
    @ApiModelProperty(name = "watchTotal", value = "观看")
    private Integer watchTotal;

    @ApiModelProperty(name = "files", value = "附件")
    private List<FileInfoVo> files;

    @ApiModelProperty(name = "productHomeImageMd5", value = "产品首页展示图MD5", allowEmptyValue = true)
    private String productHomeImageMd5;

    /**
     * 项目相关平台及组件
     */
    @ApiModelProperty(name = "platformAndModule", value = "项目相关平台及组件")
    private String platformAndModule;
}
