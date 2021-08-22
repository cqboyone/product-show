package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品信息(ProductInfo)表插入校验实体
 *
 * @author vv
 * @since 2021-06-04 09:50:52
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductInfoInsertValidator", description = "产品信息插入校验实体")
public class ProductInfoInsertValidator implements Serializable {
    private static final long serialVersionUID = -17203464073078014L;

    @ApiModelProperty(name = "productName", value = "产品名称", allowEmptyValue = true)
    private String productName;

    @ApiModelProperty(name = "productBusiness", value = "所属行业", allowEmptyValue = true)
    private String productBusiness;

    @ApiModelProperty(name = "productArea", value = "所属区域", allowEmptyValue = true)
    private String productArea;

    @ApiModelProperty(name = "productLinkmanTel", value = "产品联系人电话", allowEmptyValue = true)
    private String productLinkmanTel;

    @ApiModelProperty(name = "productLinkman", value = "产品联系人", allowEmptyValue = true)
    private String productLinkman;

    @ApiModelProperty(name = "productIntroduction", value = "产品简介", allowEmptyValue = true)
    private String productIntroduction;

    @ApiModelProperty(name = "productLightSpot", value = "产品亮点", allowEmptyValue = true)
    private String productLightSpot;

    @ApiModelProperty(name = "files", value = "文件列表", allowEmptyValue = true)
    private List<ProductFileUniqueInsertValidator> files;

    @ApiModelProperty(name = "platformAndModule", value = "项目相关平台及组件", allowEmptyValue = true)
    private String platformAndModule;
}
