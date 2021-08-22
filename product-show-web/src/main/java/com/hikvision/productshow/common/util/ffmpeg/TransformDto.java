package com.hikvision.productshow.common.util.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @description:
 * @creator vv
 * @date 2021/6/7 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformDto {
    private String md5;
    /**
     * md5文件名
     */
    private String md5Name;
    /**
     * minio中文件真实路径
     */
    private String minIOPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransformDto that = (TransformDto) o;
        return Objects.equals(md5, that.md5) && Objects.equals(md5Name, that.md5Name) && Objects.equals(minIOPath, that.minIOPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(md5, md5Name, minIOPath);
    }
}
