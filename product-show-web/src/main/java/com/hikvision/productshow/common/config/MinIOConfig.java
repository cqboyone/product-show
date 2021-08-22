package com.hikvision.productshow.common.config;

import com.hikvision.productshow.common.prop.MinIOProp;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @creator vv
 * @date 2021/6/4 14:39
 */
@Configuration
public class MinIOConfig {

    @Autowired
    private MinIOProp minIOProp;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(minIOProp.getEndpoint())
                        .credentials(minIOProp.getAccessKey(), minIOProp.getSecretKey())
                        .build();
        return minioClient;
    }
}
