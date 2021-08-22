package com.hikvision.productshow.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.productshow.common.constants.FileConstants;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.util.ffmpeg.FilePathHandler;
import com.hikvision.productshow.module.dao.FileInfoDao;
import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.entity.ProductFileUnique;
import com.hikvision.productshow.module.mapstruct.FileInfoMapping;
import com.hikvision.productshow.module.service.FileInfoService;
import com.hikvision.productshow.module.service.ProductFileUniqueService;
import com.hikvision.productshow.module.vo.FileInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hikvision.productshow.common.enums.Column.FileInfo.FILE_MD5;
import static com.hikvision.productshow.common.enums.CommonErrorCode.DELETE_FILE_FILED;
import static com.hikvision.productshow.common.enums.CommonErrorCode.FILE_NOT_EXISTS;

/**
 * 文件信息(FileInfo)表服务实现类
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
@Slf4j
@Service("fileInfoService")
public class FileInfoServiceImpl extends ServiceImpl<FileInfoDao, FileInfo> implements FileInfoService {

    @Autowired
    private ProductFileUniqueService productFileUniqueService;

    @Resource
    private FileInfoMapping fileInfoMapping;

    @Resource
    private FilePathHandler filePathHandler;

    @Resource
    private MinIOService minIOService;

    /**
     * 查询该文件是否已有记录
     *
     * @param md5
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public boolean checkExistsFile(String md5) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FILE_MD5.column, md5);
        int count = count(queryWrapper);
        return count > 0;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<FileInfo> listByMd5(List<String> md5s) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(FILE_MD5.column, md5s);
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public void checkFileAllExists(List<String> md5s) {
        //校验文件合法性
        if (this.listByMd5(md5s).size() != md5s.size()) {
            throw new ApiException(FILE_NOT_EXISTS);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<FileInfoVo> getFileInfoVoByProductId(String productId) {
        //根据产品id查询出该产品的产品附件关联表
        List<ProductFileUnique> productFileUniques = this.productFileUniqueService.findByProductId(productId);
        if (ObjectUtils.isEmpty(productFileUniques)) {
            return null;
        }
        //拿出关联的md5，查询出对应的文件信息
        List<String> md5s = productFileUniques.stream().map(ProductFileUnique::getFileMd5).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(md5s)) {
            return null;
        }
        List<FileInfoVo> fileInfoVos = this.listByMd5(md5s).stream()
                .map(e -> fileInfoMapping.po2Vo(e)).collect(Collectors.toList());
        //描述信息需要从productFileUniques复制过来
        Map<String, String> collect = productFileUniques.stream()
                .collect(Collectors.toMap(ProductFileUnique::getFileMd5, ProductFileUnique::getDetail));
        fileInfoVos.forEach(e -> e.setDetail(collect.get(e.getFileMd5())));
        return fileInfoVos;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void removeByMd5s(List<String> md5List) {
        if (ObjectUtils.isEmpty(md5List)) {
            return;
        }
        List<FileInfo> fileInfos = this.listByMd5(md5List);
        //删除数据库
        this.removeByIds(fileInfos.stream().map(e -> e.getId()).collect(Collectors.toList()));
        fileInfos.stream()
                .forEach(e -> {
                    try {
                        //删除原文件
                        minIOService.deleteFile(e.getFileType(), e.getNewName());
                        //删除转码文件
                        if (FileConstants.FILE_TYPE_VIDEO.equals(e.getFileType())) {
                            delTranscodingFile(e.getFileMd5());
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        throw new ApiException(DELETE_FILE_FILED);
                    }
                });
    }

    public void delTranscodingFile(String md5) throws IOException {
        FileUtils.deleteDirectory(new File(filePathHandler.getOutDirForName(md5)));
    }
}
