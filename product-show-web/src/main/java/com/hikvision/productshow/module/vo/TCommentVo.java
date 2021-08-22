package com.hikvision.productshow.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论信息(TComment)表展示
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TCommentVo", description = "展示评论信息")
public class TCommentVo implements Serializable {
    private static final long serialVersionUID = -95839543950748262L;
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
     * 评论
     */
    @ApiModelProperty(name = "comment", value = "评论")
    private String comment;
    /**
     * ip
     */
    @ApiModelProperty(name = "ip", value = "ip")
    private String ip;
    /**
     * 产品id
     */
    @ApiModelProperty(name = "productId", value = "产品id")
    private String productId;
    /**
     * 评论父级id
     */
    @ApiModelProperty(name = "cParentId", value = "评论父级id")
    private String cParentId;
    /**
     * 子级评论
     */
    @ApiModelProperty(name = "child", value = "子级评论")
    private List<TCommentVo> child;
}
