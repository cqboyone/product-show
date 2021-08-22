package com.hikvision.productshow.module.service.impl;

import com.hikvision.productshow.common.config.FileProperties;
import com.hikvision.productshow.common.enums.CommonErrorCode;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.module.entity.FileInfo;
import com.hikvision.productshow.module.service.FileAbstractService;
import com.hikvision.productshow.module.service.FileInfoService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.hikvision.productshow.common.enums.CommonErrorCode.FILE_NOT_EXISTS;


/**
 * @description:
 * @creator vv
 * @date 2021/5/17 20:12
 */
@Service
public class FileService extends FileAbstractService {

    @Autowired
    private FileProperties fileProperties;

    @Resource
    private FileInfoService fileInfoService;

    public String uploadFile(String type, MultipartFile file) {
        // 开始文件上传
        FileOutputStream fileOutputStream = null;
        try {
            // 获得文件上传的文件名称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                throw new ApiException(CommonErrorCode.FILE_NAME_IS_NULL);
            }
            // 文件重命名  1.png -> ["1", "png"]
            String fileNameArr[] = fileName.split("\\.");
            // 获取文件的后缀名
            String suffix = fileNameArr[fileNameArr.length - 1];
            checkFileType(type, suffix);
            // 文件名
            String newFileName = Sid.next() + "." + suffix;
            // 日期yyyy-MM-dd
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            // 相对路径
            String relative = type + File.separator + format + File.separator + newFileName;
            // 最终保存的位置
            String finalFacePath = fileProperties.getFileSpace() + File.separator + relative;
            File outFile = new File(finalFacePath);
            if (outFile.getParentFile() != null) {
                // 创建文件夹
                outFile.getParentFile().mkdirs();
            }
            // 文件输出保存到目录
            fileOutputStream = new FileOutputStream(outFile);
            InputStream inputStream = file.getInputStream();
            IOUtils.copy(inputStream, fileOutputStream);
//            return fileProperties.getStaticRoot() + "/" + type + "/" + format + "/" + newFileName;
            return finalFacePath;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(CommonErrorCode.FILE_UPLOAD_FAIL);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void downloadFile(String md5, HttpServletResponse response) {
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
            FileUtils.copyFile(new File(fileInfos.get(0).getRealPath()), response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
