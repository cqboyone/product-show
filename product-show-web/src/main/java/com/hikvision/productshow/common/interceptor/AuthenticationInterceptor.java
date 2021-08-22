package com.hikvision.productshow.common.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hikvision.productshow.common.annotation.PassToken;
import com.hikvision.productshow.common.annotation.UserLoginToken;
import com.hikvision.productshow.common.exception.ApiException;
import com.hikvision.productshow.common.threadLocal.UserHolder;
import com.hikvision.productshow.module.entity.TUser;
import com.hikvision.productshow.module.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.hikvision.productshow.common.enums.CommonErrorCode.LOGIN_FILED_USER_NOT_EXISTS;
import static com.hikvision.productshow.common.enums.CommonErrorCode.TOKEN_IS_ERROR;
import static com.hikvision.productshow.common.enums.CommonErrorCode.TOKEN_IS_ILLEGAL;
import static com.hikvision.productshow.common.enums.CommonErrorCode.TOKEN_IS_NULL;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private TUserService tUserService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new ApiException(TOKEN_IS_NULL);
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new ApiException(TOKEN_IS_ERROR);
                }
                TUser user = tUserService.getById(userId);
                if (user == null) {
                    throw new ApiException(LOGIN_FILED_USER_NOT_EXISTS);
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new ApiException(TOKEN_IS_ILLEGAL);
                }
                UserHolder.holdUser(user);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
