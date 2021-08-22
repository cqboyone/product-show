package com.hikvision.productshow.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论信息(TComment)表实体类
 *
 * @author vv
 * @since 2021-05-24 15:56:48
 */
@SuppressWarnings("serial")
@Data
public class TComment extends Model<TComment> {
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
     * 用户名
     */
    private String userName;
    /**
     * 评论
     */
    private String comment;
    /**
     * ip
     */
    private String ip;
    /**
     * 产品id
     */
    private String productId;

    /**
     * 评论父级id
     * c_parent_id
     */
    private String cParentId;

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
