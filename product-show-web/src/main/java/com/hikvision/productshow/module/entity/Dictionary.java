package com.hikvision.productshow.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典(Dictionary)表实体类
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@SuppressWarnings("serial")
@Data
public class Dictionary extends Model<Dictionary> {
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
     * 类型
     */
    private String dicType;
    /**
     * 父级节点key
     */
    private String dicParentKey;
    /**
     * key
     */
    private String dicKey;
    /**
     * value
     */
    private String dicValue;

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
