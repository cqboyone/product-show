package com.hikvision.productshow.module.service.impl;

import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.util.FileUtil;
import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.service.FileAbstractService;
import com.hikvision.productshow.module.service.FileInfoService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static com.hikvision.productshow.common.enums.CommonErrorCode.FILE_NOT_EXISTS;
import static io.minio.ObjectWriteArgs.MIN_MULTIPART_SIZE;

/**
 * @description:
 * @creator vv
 * @date 2021/6/4 15:38
 */
@Service
@Slf4j
public class MinIOService extends FileAbstractService {

    @Autowired
    private MinioClient minioClient;

    @Resource
    private FileInfoService fileInfoService;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("存储桶 {} 不存在", bucketName);
            return false;
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    public boolean makeBucket(String bucketName) {
        try {
            //存储桶不存在则创建存储桶
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;
        } catch (Exception e) {
            log.error("创建存储桶 {} 失败", bucketName);
            return false;
        }
    }

    /**
     * 文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean fileExists(String bucketName, String fileName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName).object(fileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("文件 {}-{} 不存在", bucketName, bucketName);
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param file
     * @return
     */
    @Override
    public String uploadFile(String bucketName, MultipartFile file) {
        makeBucket(bucketName);
        InputStream in = null;
        try {
            in = file.getInputStream();
            PutObjectArgs objectArgs =
                    PutObjectArgs.builder()
                            .stream(in, file.getSize(), MIN_MULTIPART_SIZE)
                            .bucket(bucketName)
                            //这里重命名一下
                            .object(FileUtil.renameFileAsMd5(file))
                            .build();
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return FileUtil.getMd5(file);
    }

    @Override
    public void downloadFile(String md5, HttpServletResponse response) {
        InputStream in = null;
        //根据md5查询文件信息
        List<FileInfo> fileInfos = this.fileInfoService.listByMd5(Arrays.asList(md5));
        if (ObjectUtils.isEmpty(fileInfos)) {
            throw new ApiException(FILE_NOT_EXISTS);
        }
        String beforeName = getBeforeName(fileInfos.get(0));
        if (StringUtils.isBlank(beforeName)) {
            throw new ApiException(FILE_NOT_EXISTS);
        }
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(beforeName, "UTF-8"));
            in = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(fileInfos.get(0).getFileType())
                            .object(fileInfos.get(0).getFileMd5() + "." + FileUtil.getSuffix(beforeName))
                            .build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param fileName
     */
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            log.error("删除文件 {}-{} 失败", bucketName, fileName);
        }
    }

}
