package com.hikvision.productshow.common.util;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @description:
 * @creator vv
 * @date 2021/5/21 16:48
 */
public class ForEachUtils {

    /**
     * @param <T>
     * @param startIndex 开始遍历的索引
     * @param elements   集合
     * @param action
     */
    public static <T> void forEach(int startIndex, Iterable<? extends T> elements, BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (T element : elements) {
            action.accept(startIndex, element);
            startIndex++;
        }
    }
}