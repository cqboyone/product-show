package com.hikvision.productshow.common.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @creator vv
 * @date 2021/5/20 11:32
 */

@ApiModel(description = "提供的统一分页返回格式")
public class BasePage<T> implements Serializable {
    private static final long serialVersionUID = 4221425786302636919L;

    @ApiModelProperty(required = true, value = "数据总条数", dataType = "Long", example = "100")
    private Long total;

    @ApiModelProperty(required = true, value = "当前页 数据内容")
    private List<T> list;

    @ApiModelProperty(required = true, value = "页面大小", dataType = "Long", example = "20")
    private Long pageSize;

    @ApiModelProperty(required = true, value = "当前页码", dataType = "Long", example = "1")
    private Long pageNo;

    public BasePage() {
    }

    public BasePage(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
