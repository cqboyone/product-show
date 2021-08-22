package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.validator.FileInfoInsertValidator;
import com.hikvision.productshow.module.validator.FileInfoUpdateValidator;
import com.hikvision.productshow.module.vo.FileInfoVo;
import org.mapstruct.Mapper;

/**
 * 文件信息(FileInfo)表实体转换类
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@Mapper(componentModel = "spring")
public interface FileInfoMapping {

    FileInfoVo po2Vo(FileInfo po);

    FileInfo insert2Po(FileInfoInsertValidator v);

    FileInfo update2Po(FileInfoUpdateValidator v);
}
