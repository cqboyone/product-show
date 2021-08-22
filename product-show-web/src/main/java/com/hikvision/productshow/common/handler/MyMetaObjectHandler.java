package com.hikvision.productshow.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @creator vv
 * @date 2021/5/19 17:18
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Date now = new Date();
        this.strictInsertFill(metaObject, "createdTime", Date.class, now); // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "updatedTime", Date.class, now); // 起始版本 3.3.0(推荐)
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updatedTime", Date.class, new Date()); // 起始版本 3.3.0(推荐)
    }

//    /**
//     * 严格模式填充策略,默认有值不覆盖,如果提供的值为null也不填充。重写该方法解决允许为空的时候自动填充不填充的问题。
//     *
//     * @param metaObject metaObject meta object parameter
//     * @param fieldName  java bean property name
//     * @param fieldVal   java bean property value of Supplier
//     * @return this
//     * @since 3.3.0
//     */
//    @Override
//    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
//        Object obj = fieldVal.get();
//        if (Objects.nonNull(obj)) {
//            metaObject.setValue(fieldName, obj);
//        }
//        return this;
//    }
}
