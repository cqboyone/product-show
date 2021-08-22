package com.hikvision.productshow.module.vo;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 字典(Dictionary)表展示
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "DictionaryVo", description = "展示字典")
public class DictionaryVo implements Serializable {
    private static final long serialVersionUID = -18376435326137798L;
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
     * 类型
     */
    @ApiModelProperty(name = "dicType", value = "类型")
    private String dicType;
    /**
     * 父级节点key
     */
    @ApiModelProperty(name = "dicParentKey", value = "父级节点key")
    private String dicParentKey;
    /**
     * key
     */
    @ApiModelProperty(name = "dicKey", value = "key")
    private String dicKey;
    /**
     * value
     */
    @ApiModelProperty(name = "dicValue", value = "value")
    private String dicValue;
}
