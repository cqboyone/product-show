package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.module.dao.TCommentDao;
import com.hikvision.productshow.module.entity.TComment;
import com.hikvision.productshow.module.service.TCommentService;
import org.springframework.stereotype.Service;

/**
 * 评论信息(TComment)表服务实现类
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@Service("tCommentService")
public class TCommentServiceImpl extends ServiceImpl<TCommentDao, TComment> implements TCommentService {

}
