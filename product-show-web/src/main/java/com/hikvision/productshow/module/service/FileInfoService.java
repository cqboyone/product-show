package com.hikvision.productshow.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.vo.FileInfoVo;

import java.util.List;

/**
 * 文件信息(FileInfo)表服务接口
 *
 * @author vv
 * @since 2021-05-21 10:29:10
 */
public interface FileInfoService extends IService<FileInfo> {

    /**
     * 查询该文件是否已有记录
     *
     * @param md5
     * @return
     */
    boolean checkExistsFile(String md5);

    List<FileInfo> listByMd5(List<String> md5s);

    /**
     * 检查是否所有的文件都存在
     *
     * @param md5s
     */
    void checkFileAllExists(List<String> md5s);

    /**
     * 根据产品id查询关联的文件信息
     *
     * @param productId
     * @return
     */
    List<FileInfoVo> getFileInfoVoByProductId(String productId);

    void removeByMd5s(List<String> md5List);
}
