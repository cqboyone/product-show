package com.hikvision.productshow.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品信息(ProductInfo)表实体类
 *
 * @author vv
 * @since 2021-06-04 09:50:50
 */
@SuppressWarnings("serial")
@Data
public class ProductInfo extends Model<ProductInfo> {
    /**
     * 主键
     */
    private String id;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 所属行业
     */
    private String productBusiness;
    /**
     * 所属区域
     */
    private String productArea;
    /**
     * 产品联系人电话
     */
    private String productLinkmanTel;
    /**
     * 产品联系人
     */
    private String productLinkman;
    /**
     * 产品简介
     */
    private String productIntroduction;
    /**
     * 产品亮点
     */
    private String productLightSpot;
    /**
     * 点赞
     */
    private Integer likeTotal;
    /**
     * 观看
     */
    private Integer watchTotal;

    /**
     * 产品首页展示图MD5
     */
    private String productHomeImageMd5;

    /**
     * 版本
     */
    @Version
    private Integer version;
    /**
     * 项目相关平台及组件
     */
    private String platformAndModule;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
