package com.hikvision.productshow.module.mapstruct;

import com.hikvision.productshow.module.entity.TComment;
import com.hikvision.productshow.module.validator.TCommentInsertValidator;
import com.hikvision.productshow.module.validator.TCommentUpdateValidator;
import com.hikvision.productshow.module.vo.TCommentVo;
import org.mapstruct.Mapper;

/**
 * 评论信息(TComment)表实体转换类
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@Mapper(componentModel = "spring")
public interface TCommentMapping {

    TCommentVo po2Vo(TComment po);

    TComment insert2Po(TCommentInsertValidator v);

    TComment update2Po(TCommentUpdateValidator v);
}
