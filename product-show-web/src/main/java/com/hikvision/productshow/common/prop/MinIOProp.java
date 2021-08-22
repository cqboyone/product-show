package com.hikvision.productshow.common.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @creator vv
 * @date 2021/6/4 14:41
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
@Configuration
public class MinIOProp {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
