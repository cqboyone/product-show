package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 评论信息(TComment)表分页查询校验实体
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TCommentPageValidator", description = "评论信息分页查询校验实体")
public class TCommentPageValidator extends BasePageValidator implements Serializable {
    private static final long serialVersionUID = -40340174395976555L;

    @NotBlank
    @ApiModelProperty(name = "productId", value = "产品主键", allowEmptyValue = true, example = "")
    private String productId;

}
