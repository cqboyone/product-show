package com.hikvision.productshow.common.enums;

/**
 * @description:
 * @creator vv
 * @date 2021/5/22 10:14
 */
public class Column {
    public enum FileInfo {
        PRODUCT_ID("product_id"),
        FILE_MD5("file_md5"),
        ;
        public String column;

        FileInfo(String column) {
            this.column = column;
        }
    }
}
