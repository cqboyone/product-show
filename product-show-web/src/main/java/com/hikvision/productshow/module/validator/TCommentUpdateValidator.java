package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 评论信息(TComment)表更新校验实体
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TCommentUpdateValidator", description = "评论信息更新校验实体")
public class TCommentUpdateValidator extends TCommentInsertValidator implements Serializable {
    private static final long serialVersionUID = -39521394271403617L;

    @NotBlank
    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

}
