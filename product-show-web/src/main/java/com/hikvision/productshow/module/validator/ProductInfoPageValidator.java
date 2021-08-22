package com.hikvision.productshow.module.validator;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品信息(ProductInfo)表分页查询校验实体
 *
 * @author vv
 * @since 2021-05-20 16:46:09
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductInfoPageValidator", description = "产品信息分页查询校验实体")
public class ProductInfoPageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = 954703340502198541L;

    @ApiModelProperty(name = "createdTimeStart", value = "创建时间开始", example = "2021-05-25 08:00:00")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTimeStart;

    @ApiModelProperty(name = "createdTimeEnd", value = "创建时间结束", example = "2021-05-25 08:00:00")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTimeEnd;

    @ApiModelProperty(name = "key", value = "融合参数", example = "")
    private String key;

    @ApiModelProperty(name = "isCreatedTimeAsc", value = "创建时间是否升序，填入boolean值，默认倒序", example = "false")
    private Boolean isCreatedTimeAsc = false;

    @ApiModelProperty(name = "productArea", value = "所属区域", example = "")
    private String productArea;

    @ApiModelProperty(name = "isLikeTotalAsc", value = "点赞数量是否升序，需要排序填入boolean值，不需要就不填", example = "false")
    private Boolean isLikeTotalAsc;

    @ApiModelProperty(name = "productBusiness", value = "所属行业", example = "")
    private String productBusiness;

}
