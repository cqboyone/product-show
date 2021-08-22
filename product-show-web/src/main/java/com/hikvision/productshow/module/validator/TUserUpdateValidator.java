package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户信息(TUser)表更新校验实体
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "TUserUpdateValidator", description = "用户信息更新校验实体")
public class TUserUpdateValidator extends TUserInsertValidator implements Serializable {
    private static final long serialVersionUID = 622524960998786245L;

    @NotBlank
    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

}
