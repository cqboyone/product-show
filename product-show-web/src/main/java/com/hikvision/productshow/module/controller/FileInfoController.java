package com.hikvision.productshow.module.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.productshow.common.BO.BasePage;
import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.annotation.UserLoginToken;
import com.hikvision.productshow.common.enums.CommonErrorCode;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.util.FileUtil;
import com.hikvision.productshow.common.util.ffmpeg.FilePathHandler;
import com.hikvision.productshow.common.util.ffmpeg.VideoTransformHandlerV2;
import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.mapstruct.FileInfoMapping;
import com.hikvision.productshow.module.service.FileInfoService;
import com.hikvision.productshow.module.service.impl.MinIOService;
import com.hikvision.productshow.module.validator.FileInfoPageValidator;
import com.hikvision.productshow.module.vo.FileInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.constants.FileConstants.FILE_TYPE_VIDEO;
import static com.hikvision.productshow.common.enums.CommonErrorCode.DELETE_ID_NOT_FOUND;
import static com.hikvision.productshow.common.enums.CommonErrorCode.FIND_DATA_IS_NULL;

/**
 * 文件信息(FileInfo)表控制层
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@RestController
@RequestMapping("fileInfo")
@Api(tags = "文件信息")
@Validated //要验证list需要使用该注解
@Slf4j
public class FileInfoController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private FileInfoService fileInfoService;

    /**
     * mapping
     */
    @Resource
    private FileInfoMapping fileInfoMapping;

    @Autowired
    private VideoTransformHandlerV2 videoTransformHandlerV2;

    @Resource
    private MinIOService minIOService;

    @Resource
    private FilePathHandler filePathHandler;

    @UserLoginToken
    @ApiOperation(value = "保存file", notes = "保存file")
    @PostMapping("uploadFile.do")
    public BaseResult uploadFile(@ApiParam(name = "type", value = "file type", required = true)
                                 @RequestParam String type, @RequestPart("file") MultipartFile file) {
        needAdmin();
        if (file == null) {
            throw new ApiException(CommonErrorCode.FILE_NOT_NULL);
        }
        //对文件类型判断
        minIOService.checkFileType(type);
        String md5 = FileUtil.getMd5(file);
        //检查本地是否有文件
        //文件扩展名
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String md5Name = md5 + "." + suffix;
        boolean fileExists = minIOService.fileExists(type, md5Name);
        //如果没有文件，存储
        if (!fileExists) {
            log.info("文件md5={}不存在，开始保存文件", md5);
            minIOService.uploadFile(type, file);
        }
        // 针对视频需要转码
        String sourceRealPath = filePathHandler.getFileRealPathInMinIO(type, md5Name);
        if (FILE_TYPE_VIDEO.equals(type)) {
            // 开始转码、切片。如果已经转码，直接返回true。
            videoTransformHandlerV2.put(md5, md5Name, sourceRealPath);
        }
        //检查数据库是否有该文件
        boolean existsFile = fileInfoService.checkExistsFile(md5);
        log.info("数据库是否存在该文件 {} 记录 {}", md5, existsFile);
        if (!existsFile) {
            // 文件信息入库。
            this.fileInfoService.save(buildInsert(md5, file, type, sourceRealPath, md5Name, suffix));
        }
        return BaseResult.success(md5);
    }

    /**
     * 分页查询所有数据
     *
     * @param v 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询所有数据", notes = "分页查询所有数据")
    @PostMapping("page")
    public BaseResult<BasePage<FileInfoVo>> selectAll(@Valid @RequestBody FileInfoPageValidator v) {
        Page<FileInfo> page = new Page(v.getPageNumber(), v.getPageSize());
        Page<FileInfo> infoPage = this.fileInfoService.page(page);
        BasePage<FileInfoVo> basePage = new BasePage(infoPage.getTotal(),
                infoPage.getRecords().stream().map(e -> this.fileInfoMapping.po2Vo(e)).collect(Collectors.toList()));
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
    public BaseResult<FileInfoVo> selectOne(@PathVariable @ApiParam(value = "主键") Serializable id) {
        return BaseResult.success(
                Optional.ofNullable(this.fileInfoService.getById(id))
                        .map(e -> this.fileInfoMapping.po2Vo(e))
                        .orElseThrow(() -> new ApiException(FIND_DATA_IS_NULL)));
    }

    /**
     * 删除数据
     *
     * @param md5List md5集合
     * @return 删除结果
     */
    @UserLoginToken
    @ApiOperation(value = "删除数据", notes = "包括数据库、原文件、转码文件")
    @DeleteMapping
    public BaseResult delete(@ApiParam(value = "主键列表") @RequestParam("md5List") List<String> md5List) {
        needAdmin();
        if (ObjectUtils.isEmpty(md5List)) {
            throw new ApiException(DELETE_ID_NOT_FOUND);
        }
        this.fileInfoService.removeByMd5s(md5List);
        return BaseResult.success();
    }

    @ApiOperation(value = "下载文件", notes = "下载文件")
    @GetMapping("/download/{md5}")
    public void download(@PathVariable String md5, HttpServletResponse response) {
        log.debug("下载文件md5={}", md5);
        minIOService.downloadFile(md5, response);
    }

    /**
     * 查询该文件是否已上传
     *
     * @param md5 md5
     * @return boolean
     */
    @UserLoginToken
    @ApiOperation(value = "查询该文件是否已上传", notes = "查询该文件是否已上传")
    @GetMapping("md5")
    public BaseResult<Boolean> checkMd5(@RequestParam @ApiParam(value = "md5") String md5) {
        needAdmin();
        return BaseResult.success(fileInfoService.checkExistsFile(md5));
    }

    private FileInfo buildInsert(String md5, MultipartFile file, String type, String realPath, String newName, String suffix) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileMd5(md5);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(type);
        fileInfo.setBeforeName(file.getOriginalFilename());
        fileInfo.setRealPath(realPath);
        fileInfo.setNewName(newName);
        fileInfo.setFileSuffix(suffix);
        return fileInfo;
    }
}
