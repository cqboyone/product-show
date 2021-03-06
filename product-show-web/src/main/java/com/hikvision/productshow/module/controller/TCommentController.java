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
 * ????????????(TComment)????????????
 *
 * @author vv
 * @since 2021-05-24 15:56:49
 */
@RestController
@RequestMapping("tComment")
@Api(tags = "????????????")
@Validated //?????????list?????????????????????
@Slf4j
public class TCommentController extends BaseController {
    /**
     * ????????????
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
     * ????????????????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @PostMapping("page")
    public BaseResult<BasePage<TCommentVo>> selectAll(@Valid @RequestBody TCommentPageValidator v) {
        //??????????????????
        Page<TComment> pInfoPage = this.tCommentService.page(new Page(v.getPageNumber(), v.getPageSize()),
                Wrappers.lambdaQuery(TComment.class)
                        .eq(TComment::getProductId, v.getProductId())
                        .eq(TComment::getCParentId, DEFAULT_PARENT_ID)
                        .orderByDesc(TComment::getCreatedTime)
        );
        //????????????
        List<TCommentVo> pComment = pInfoPage.getRecords().stream()
                .map(e -> this.tCommentMapping.po2Vo(e))
                .collect(Collectors.toList());
        //????????????id
        List<String> pIds = pComment.stream().map(TCommentVo::getId).collect(Collectors.toList());
        //???????????????
        Page<TComment> cInfoPage = this.tCommentService.page(new Page(v.getPageNumber(), v.getPageSize()),
                Wrappers.lambdaQuery(TComment.class)
                        .eq(TComment::getProductId, v.getProductId())
                        .in(!ObjectUtils.isEmpty(pIds), TComment::getCParentId, pIds)
                        .orderByDesc(TComment::getCreatedTime)
        );
        //?????????
        Map<String, List<TCommentVo>> cMap = cInfoPage.getRecords().stream()
                .map(e -> this.tCommentMapping.po2Vo(e))
                .collect(Collectors.groupingBy(TCommentVo::getCParentId));
        //????????????
        pComment.stream().forEach(e -> e.setChild(cMap.get(e.getId())));
        BasePage<TCommentVo> basePage = new BasePage(pInfoPage.getTotal(), pComment);
        return BaseResult.success(basePage);
    }

    /**
     * ??????????????????????????????
     *
     * @param id ??????
     * @return ????????????
     */
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
    @GetMapping("{id}")
    public BaseResult<TCommentVo> selectOne(@PathVariable @ApiParam(value = "??????") Serializable id) {
        return BaseResult.success(
                Optional.ofNullable(this.tCommentService.getById(id))
                        .map(e -> this.tCommentMapping.po2Vo(e))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL)));
    }

    /**
     * ????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
    @PostMapping
    public BaseResult insert(@Valid @RequestBody TCommentInsertValidator v, HttpServletRequest request) {
        //????????????????????????
        ProductInfo byId = productInfoService.getById(v.getProductId());
        if (byId == null) {
            throw new ApiException(PRODUCT_IS_NULL);
        }
        if (StringUtils.isNotBlank(v.getCParentId())) {
            //??????????????????????????????????????????????????????????????????
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
     * ????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
    @PutMapping
    public BaseResult update(@Valid @RequestBody TCommentUpdateValidator v) {
        this.tCommentService.updateById(this.tCommentMapping.update2Po(v));
        return BaseResult.success();
    }

    /**
     * ????????????
     *
     * @param idList ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
    @DeleteMapping
    public BaseResult delete(@ApiParam(value = "????????????") @RequestParam("idList") List<String> idList) {
        this.tCommentService.removeByIds(idList);
        return BaseResult.success();
    }
}
