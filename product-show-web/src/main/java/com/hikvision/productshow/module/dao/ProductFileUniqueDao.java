package com.hikvision.productshow.module.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hikvision.productshow.module.entity.ProductFileUnique;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (ProductFileUnique)表数据库访问层
 *
 * @author vv
 * @since 2021-05-21 16:27:56
 */
public interface ProductFileUniqueDao extends BaseMapper<ProductFileUnique> {

    @Select("select distinct product_id from product_file_unique ${ew.customSqlSegment}")
    List<String> distinctProductIdByMd5(@Param(Constants.WRAPPER) Wrapper wrapper);
}
