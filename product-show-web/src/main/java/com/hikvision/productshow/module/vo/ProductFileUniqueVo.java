package com.hikvision.productshow.module.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (ProductFileUnique)表展示
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductFileUniqueVo", description = "展示")
public class ProductFileUniqueVo implements Serializable {
    private static final long serialVersionUID = -72571131975422115L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 产品id
     */
    @ApiModelProperty(name = "productId", value = "产品id")
    private String productId;
    /**
     * 文件id
     */
    @ApiModelProperty(name = "fileMd5", value = "文件md5")
    private String fileMd5;
    /**
     * 文件顺序
     */
    @ApiModelProperty(name = "fileOrder", value = "文件顺序")
    private Integer fileOrder;
    /**
     * 描述
     */
    @ApiModelProperty(name = "detail", value = "描述")
    private String detail;
    /**
     * 文件类型
     */
    @ApiModelProperty(name = "fileType", value = "文件类型")
    private String fileType;
}
