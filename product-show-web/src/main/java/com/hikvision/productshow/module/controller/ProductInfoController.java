package com.hikvision.productshow.module.controller;

import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.annotation.UserLoginToken;
import com.hikvision.productshow.common.constants.FileConstants;
import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.mapstruct.ProductInfoMapping;
import com.hikvision.productshow.module.service.FileInfoService;
import com.hikvision.productshow.module.service.ProductFileUniqueService;
import com.hikvision.productshow.module.service.ProductInfoService;
import com.hikvision.productshow.module.validator.ProductFileUniqueInsertValidator;
import com.hikvision.productshow.module.validator.ProductInfoInsertValidator;
import com.hikvision.productshow.module.validator.ProductInfoPageValidator;
import com.hikvision.productshow.module.validator.ProductInfoUpdateValidator;
import com.hikvision.productshow.module.vo.ProductInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品信息(ProductInfo)表控制层
 *
 * @author vv
 * @since 2021-05-20 18:38:29
 */
@RestController
@RequestMapping("productInfo")
@Api(tags = "产品信息")
@Validated //要验证list需要使用该注解
@Slf4j
public class ProductInfoController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ProductInfoService productInfoService;

    /**
     * mapping
     */
    @Resource
    private ProductInfoMapping productInfoMapping;

    @Resource
    private ProductFileUniqueService productFileUniqueService;

    @Resource
    private FileInfoService fileInfoService;

    /**
     * 分页查询所有数据
     *
     * @param v 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询所有数据", notes = "分页查询所有数据")
    @PostMapping("page")
    public BaseResult<BasePage<ProductInfoVo>> selectAll(@Valid @RequestBody ProductInfoPageValidator v) {
        return BaseResult.success(this.productInfoService.pageByParams(v));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过主键查询单条数据", notes = "通过主键查询单条数据")
    @GetMapping("{id}")
    public BaseResult<ProductInfoVo> selectOne(@PathVariable @ApiParam(value = "主键") Serializable id) {
        return BaseResult.success(this.productInfoService.findProductVoByProductId(id.toString()));
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
    public BaseResult insert(@Valid @RequestBody ProductInfoInsertValidator v) {
        needAdmin();
        //校验文件合法性
        checkFileAllExists(v);
        //保存产品信息
        ProductInfo productInfo = this.productInfoMapping.insert2Po(v);
        productInfo.setProductHomeImageMd5(getFirstPhotoMd5(v.getFiles()));
        this.productInfoService.save(productInfo);
        //保存文件关联信息
        productFileUniqueService.save(productInfo.getId(), v.getFiles());
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
    public BaseResult update(@Valid @RequestBody ProductInfoUpdateValidator v) {
        needAdmin();
        //校验文件合法性
        checkFileAllExists(v);
        //保存产品信息
        ProductInfo productInfo = this.productInfoMapping.update2Po(v);
        productInfo.setProductHomeImageMd5(getFirstPhotoMd5(v.getFiles()));
        this.productInfoService.updateById(productInfo);
        //保存文件关联信息
        productFileUniqueService.save(productInfo.getId(), v.getFiles());
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
        this.productInfoService.deleteProduct(idList);
        return BaseResult.success();
    }

    /**
     * 删除文件和产品关联关系
     *
     * @return 删除结果
     */
    @UserLoginToken
    @ApiOperation(value = "删除文件和产品关联关系", notes = "删除和文件关联关系")
    @DeleteMapping("file/u")
    public BaseResult deleteFileUnique(@ApiParam(value = "产品id") @RequestParam("pId") String pId,
                                       @ApiParam(value = "md5") @RequestParam("md5") String md5) {
        needAdmin();
        //删除关联关系
        this.productFileUniqueService.deleteByProductIdAndMd5(pId, md5);
        //判断如果没有引用该文件，则删除文件。如果为视频，还需删除解码。
        if (ObjectUtils.isEmpty(this.productFileUniqueService.findProductIdsByMd5(md5))) {
            fileInfoService.removeByMd5s(Arrays.asList(md5));
        }
        return BaseResult.success();
    }

    /**
     * 校验是否是所有文件都存在
     *
     * @param v
     */
    private void checkFileAllExists(ProductInfoInsertValidator v) {
        if (!ObjectUtils.isEmpty(v.getFiles())) {
            //校验文件合法性
            List<String> md5s = v.getFiles().stream().map(e -> e.getFileMd5()).collect(Collectors.toList());
            this.fileInfoService.checkFileAllExists(md5s);
        }
    }

    /**
     * 获取图片列表的第一张图片
     *
     * @param files
     * @return
     */
    private String getFirstPhotoMd5(List<ProductFileUniqueInsertValidator> files) {
        if (ObjectUtils.isEmpty(files)) {
            return null;
        }
        return files.stream()
                .filter(e -> FileConstants.FILE_TYPE_PHOTO.equals(e.getFileType()))
                .findFirst().map(ProductFileUniqueInsertValidator::getFileMd5).orElse("");
    }
}
