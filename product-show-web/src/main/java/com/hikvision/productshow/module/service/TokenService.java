package com.hikvision.productshow.module.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hikvision.productshow.module.entity.TUser;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("TokenService")
public class TokenService {

    private Long timeOutMillis = 2 * 60 * 60 * 1000L;//1小时

    public String getToken(TUser user) {
        return JWT.create().withAudience(user.getId())// 将 user id 保存到 token 里面
                .withExpiresAt(new Date(new Date().getTime() + timeOutMillis))// 设置过期时间
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
    }
}
