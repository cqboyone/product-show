package com.hikvision.productshow.module.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.module.entity.TUser;
import com.hikvision.productshow.module.mapstruct.TUserMapping;
import com.hikvision.productshow.module.service.TUserService;
import com.hikvision.productshow.module.service.TokenService;
import com.hikvision.productshow.module.validator.TUserInsertValidator;
import com.hikvision.productshow.module.validator.TUserLoginValidator;
import com.hikvision.productshow.module.validator.TUserPageValidator;
import com.hikvision.productshow.module.validator.TUserUpdateValidator;
import com.hikvision.productshow.module.vo.TUserLoginVo;
import com.hikvision.productshow.module.vo.TUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_DATA_IS_NULL;
import static com.hikvision.productshow.common.enums.CommonErrorCode.LOGIN_FILED_USER_NOT_EXISTS;
import static com.hikvision.productshow.common.enums.CommonErrorCode.LOGIN_FILED_USER_PASSWORD_WRONG;

/**
 * ????????????(TUser)????????????
 *
 * @author vv
 * @since 2021-05-24 13:45:45
 */
@RestController
@RequestMapping("tUser")
@Api(tags = "????????????")
@Validated //?????????list?????????????????????
@Slf4j
public class TUserController extends BaseController {
    /**
     * ????????????
     */
    @Resource
    private TUserService tUserService;

    /**
     * mapping
     */
    @Resource
    private TUserMapping tUserMapping;

    @Autowired
    private TokenService tokenService;

    /**
     * ????????????????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
//    @PostMapping("page")
    public BaseResult<BasePage<TUserVo>> selectAll(@Valid @RequestBody TUserPageValidator v) {
        Page<TUser> page = new Page(v.getPageNumber(), v.getPageSize());
        Page<TUser> infoPage = this.tUserService.page(page);
        BasePage<TUserVo> basePage = new BasePage(infoPage.getTotal(),
                infoPage.getRecords().stream().map(e -> this.tUserMapping.po2Vo(e)).collect(Collectors.toList()));
        return BaseResult.success(basePage);
    }

    /**
     * ??????????????????????????????
     *
     * @param id ??????
     * @return ????????????
     */
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
//    @GetMapping("{id}")
    public BaseResult<TUserVo> selectOne(@PathVariable @ApiParam(value = "??????") Serializable id) {
        return BaseResult.success(
                Optional.ofNullable(this.tUserService.getById(id))
                        .map(e -> this.tUserMapping.po2Vo(e))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL)));
    }

    /**
     * ????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
//    @PostMapping
    public BaseResult insert(@Valid @RequestBody TUserInsertValidator v) {
        TUser tUser = this.tUserMapping.insert2Po(v);
        this.tUserService.save(tUser);
        return BaseResult.success();
    }

    /**
     * ????????????
     *
     * @param v ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
//    @PutMapping
    public BaseResult update(@Valid @RequestBody TUserUpdateValidator v) {
        this.tUserService.updateById(this.tUserMapping.update2Po(v));
        return BaseResult.success();
    }

    /**
     * ????????????
     *
     * @param idList ????????????
     * @return ????????????
     */
    @ApiOperation(value = "????????????", notes = "????????????")
//    @DeleteMapping
    public BaseResult delete(@ApiParam(value = "????????????") @RequestParam("idList") List<String> idList) {
        this.tUserService.removeByIds(idList);
        return BaseResult.success();
    }

    /**
     * ??????
     *
     * @param v
     * @return
     */
    @ApiOperation(value = "??????", notes = "??????")
    @PostMapping("/login")
    public BaseResult<TUserLoginVo> login(@Valid @RequestBody TUserLoginValidator v) {
        TUser userForBase = tUserService.findByUserName(v.getUserName());
        if (userForBase == null) {
            throw new ApiException(LOGIN_FILED_USER_NOT_EXISTS);
        }
        if (!userForBase.getPassword().equals(v.getPassword())) {
            throw new ApiException(LOGIN_FILED_USER_PASSWORD_WRONG);
        }
        String token = tokenService.getToken(userForBase);
        TUserLoginVo loginVo = TUserLoginVo.builder().userName(v.getUserName()).token(token).build();
        return BaseResult.success(loginVo);
    }
}
