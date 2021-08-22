package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 评论信息(TComment)表插入校验实体
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TCommentInsertValidator", description = "评论信息插入校验实体")
public class TCommentInsertValidator implements Serializable {
    private static final long serialVersionUID = -53637915720229614L;

    @NotBlank
    @ApiModelProperty(name = "comment", value = "评论", allowEmptyValue = false)
    private String comment;

    @NotBlank
    @ApiModelProperty(name = "productId", value = "产品id", allowEmptyValue = false)
    private String productId;

    @ApiModelProperty(name = "cParentId", value = "评论父级id", allowEmptyValue = true)
    private String cParentId;

}
