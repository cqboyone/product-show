package com.hikvision.productshow.module.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.threadLocal.UserHolder;
import com.hikvision.productshow.common.util.IPUtil;
import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.entity.TComment;
import com.hikvision.productshow.module.mapstruct.TCommentMapping;
import com.hikvision.productshow.module.service.ProductInfoService;
import com.hikvision.productshow.module.service.TCommentService;
import com.hikvision.productshow.module.validator.TCommentInsertValidator;
import com.hikvision.productshow.module.validator.TCommentPageValidator;
import com.hikvision.productshow.module.validator.TCommentUpdateValidator;
import com.hikvision.productshow.module.vo.TCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.constants.CommentConstants.DEFAULT_PARENT_ID;
import static com.hikvision.productshow.common.enums.CommonErrorCode.BE_COMMENT_IS_NULL;
import static com.hikvision.productshow.common.enums.CommonErrorCode.COMMENT_MORE_THEN_TWO_TIER;
import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_DATA_IS_NULL;
import static com.hikvision.productshow.common.enums.CommonErrorCode.PRODUCT_IS_NULL;

/**
 * 评论信息(TComment)表控制层
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@RestController
@RequestMapping("tComment")
@Api(tags = "评论信息")
@Validated //要验证list需要使用该注解
@Slf4j
public class TCommentController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private TCommentService tCommentService;

    /**
     * mapping
     */
    @Resource
    private TCommentMapping tCommentMapping;

    @Resource
    private ProductInfoService productInfoService;

    /**
     * 分页查询所有数据
     *
     * @param v 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询所有数据", notes = "分页查询所有数据")
    @PostMapping("page")
    public BaseResult<BasePage<TCommentVo>> selectAll(@Valid @RequestBody TCommentPageValidator v) {
        //查询父级评论
        Page<TComment> pInfoPage = this.tCommentService.page(new Page(v.getPageNumber(), v.getPageSize()),
                Wrappers.lambdaQuery(TComment.class)
                        .eq(TComment::getProductId, v.getProductId())
                        .eq(TComment::getCParentId, DEFAULT_PARENT_ID)
                        .orderByDesc(TComment::getCreatedTime)
        );
        //父级评论
        List<TCommentVo> pComment = pInfoPage.getRecords().stream()
                .map(e -> this.tCommentMapping.po2Vo(e))
                .collect(Collectors.toList());
        //父级评论id
        List<String> pIds = pComment.stream().map(TCommentVo::getId).collect(Collectors.toList());
        //查询子评论
        Page<TComment> cInfoPage = this.tCommentService.page(new Page(v.getPageNumber(), v.getPageSize()),
                Wrappers.lambdaQuery(TComment.class)
                        .eq(TComment::getProductId, v.getProductId())
                        .in(!ObjectUtils.isEmpty(pIds), TComment::getCParentId, pIds)
                        .orderByDesc(TComment::getCreatedTime)
        );
        //子评论
        Map<String, List<TCommentVo>> cMap = cInfoPage.getRecords().stream()
                .map(e -> this.tCommentMapping.po2Vo(e))
                .collect(Collectors.groupingBy(TCommentVo::getCParentId));
        //整理结构
        pComment.stream().forEach(e -> e.setChild(cMap.get(e.getId())));
        BasePage<TCommentVo> basePage = new BasePage(pInfoPage.getTotal(), pComment);
        return BaseResult.success(basePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过主键查询单条数据", notes = "通过主键查询单条数据")
    @GetMapping("{id}")
    public BaseResult<TCommentVo> selectOne(@PathVariable @ApiParam(value = "主键") Serializable id) {
        return BaseResult.success(
                Optional.ofNullable(this.tCommentService.getById(id))
                        .map(e -> this.tCommentMapping.po2Vo(e))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL)));
    }

    /**
     * 新增数据
     *
     * @param v 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping
    public BaseResult insert(@Valid @RequestBody TCommentInsertValidator v, HttpServletRequest request) {
        //校验产品是否存在
        ProductInfo byId = productInfoService.getById(v.getProductId());
        if (byId == null) {
            throw new ApiException(PRODUCT_IS_NULL);
        }
        if (StringUtils.isNotBlank(v.getCParentId())) {
            //校验是否是评论的父级评论，目前只支持二级评论
            TComment pComment = tCommentService.getById(v.getCParentId());
            if (pComment == null) {
                throw new ApiException(BE_COMMENT_IS_NULL);
            }
            if (!DEFAULT_PARENT_ID.equals(pComment.getCParentId())) {
                throw new ApiException(COMMENT_MORE_THEN_TWO_TIER);
            }
        } else {
            v.setCParentId(DEFAULT_PARENT_ID);
        }
        TComment tComment = this.tCommentMapping.insert2Po(v);
        tComment.setUserName(UserHolder.getUserName());
        tComment.setIp(IPUtil.getIp2(request));
        this.tCommentService.save(tComment);
        return BaseResult.success();
    }

    /**
     * 修改数据
     *
     * @param v 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @PutMapping
    public BaseResult update(@Valid @RequestBody TCommentUpdateValidator v) {
        this.tCommentService.updateById(this.tCommentMapping.update2Po(v));
        return BaseResult.success();
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @DeleteMapping
    public BaseResult delete(@ApiParam(value = "主键列表") @RequestParam("idList") List<String> idList) {
        this.tCommentService.removeByIds(idList);
        return BaseResult.success();
    }
}
