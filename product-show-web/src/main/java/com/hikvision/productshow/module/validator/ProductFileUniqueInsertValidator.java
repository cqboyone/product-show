package com.hikvision.productshow.module.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * (ProductFileUnique)表插入校验实体
 *
 * @author vv
 * @since 2021-05-21 16:27:57
 */
@SuppressWarnings("serial")
@Data
@ApiModel(value = "ProductFileUniqueInsertValidator", description = "插入校验实体")
public class ProductFileUniqueInsertValidator implements Serializable {
    private static final long serialVersionUID = -60632057978783016L;

    @ApiModelProperty(name = "id", value = "主键", allowEmptyValue = true, example = "")
    private String id;

    @ApiModelProperty(name = "productId", value = "产品id", allowEmptyValue = true)
    private String productId;

    @NotBlank
    @ApiModelProperty(name = "fileMd5", value = "文件md5", allowEmptyValue = true)
    private String fileMd5;

    @Min(value = 0, message = "参数fileOrder不能小于0")
    @ApiModelProperty(name = "fileOrder", value = "文件顺序", allowEmptyValue = true)
    private Integer fileOrder;

    @ApiModelProperty(name = "detail", value = "描述", allowEmptyValue = true)
    private String detail;

    @NotBlank
    @ApiModelProperty(name = "fileType", value = "文件类型", allowEmptyValue = true)
    private String fileType;

}
