package com.hikvision.productshow.module.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (ProductFileUnique)表实体类
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
@SuppressWarnings("serial")
@Data
public class ProductFileUnique extends Model<ProductFileUnique> {
    /**
     * 主键
     */
    private String id;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 文件md5
     */
    private String fileMd5;
    /**
     * 文件顺序
     */
    private Integer fileOrder;
    /**
     * 描述
     */
    private String detail;
    /**
     * 文件类型
     */
    private String fileType;

}
