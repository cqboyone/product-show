package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BasePageValidator {

    @Min(value = 1, message = "当前页码必须为正整数")
    @ApiModelProperty(name = "pageNumber", value = "页码，从1开始", example = "1", allowEmptyValue = false)
    @NotNull(message = "pageNumber不能为空")
    private Integer pageNumber;

    @Min(value = 1, message = "每页条数必须为正整数")
    @ApiModelProperty(name = "pageSize", value = "每页数据条数", example = "20", allowEmptyValue = false)
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
