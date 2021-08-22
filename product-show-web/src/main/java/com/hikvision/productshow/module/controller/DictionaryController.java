package com.hikvision.productshow.module.controller;

import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.annotation.UserLoginToken;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.module.entity.Dictionary;
import com.hikvision.productshow.module.mapstruct.DictionaryMapping;
import com.hikvision.productshow.module.service.DictionaryService;
import com.hikvision.productshow.module.validator.DictionaryInsertValidator;
import com.hikvision.productshow.module.validator.DictionaryPageValidator;
import com.hikvision.productshow.module.validator.DictionaryUpdateValidator;
import com.hikvision.productshow.module.vo.DictionaryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_DATA_IS_NULL;


/**
 * 字典(Dictionary)表控制层
 *
 * @author vv
 * @since 2021-06-01 09:35:21
 */
@RestController
@RequestMapping("dictionary")
@Api(tags = "字典")
@Validated //要验证list需要使用该注解
@Slf4j
public class DictionaryController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private DictionaryService dictionaryService;

    /**
     * mapping
     */
    @Resource
    private DictionaryMapping dictionaryMapping;

    /**
     * 分页查询所有数据
     *
     * @param v 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询所有数据", notes = "分页查询所有数据")
    @PostMapping("page")
    public BaseResult<BasePage<DictionaryVo>> selectAll(@Valid @RequestBody DictionaryPageValidator v) {
        return BaseResult.success(this.dictionaryService.page(v));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过主键查询单条数据", notes = "通过主键查询单条数据")
    @GetMapping("{id}")
    public BaseResult<DictionaryVo> selectOne(@PathVariable @ApiParam(value = "主键") Serializable id) {
        return BaseResult.success(
                Optional.ofNullable(this.dictionaryService.getById(id))
                        .map(e -> this.dictionaryMapping.po2Vo(e))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL)));
    }

    /**
     * 新增数据
     *
     * @param v 实体对象
     * @return 新增结果
     */
    @UserLoginToken
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping
    public BaseResult insert(@Valid @RequestBody DictionaryInsertValidator v) {
        needAdmin();
        Dictionary dictionary = this.dictionaryMapping.insert2Po(v);
        this.dictionaryService.save(dictionary);
        return BaseResult.success();
    }

    /**
     * 修改数据
     *
     * @param v 实体对象
     * @return 修改结果
     */
    @UserLoginToken
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @PutMapping
    public BaseResult update(@Valid @RequestBody DictionaryUpdateValidator v) {
        needAdmin();
        this.dictionaryService.updateById(this.dictionaryMapping.update2Po(v));
        return BaseResult.success();
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @UserLoginToken
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @DeleteMapping
    public BaseResult delete(@ApiParam(value = "主键列表") @RequestParam("idList") List<String> idList) {
        needAdmin();
        this.dictionaryService.removeByIds(idList);
        return BaseResult.success();
    }
}
