package com.chockwa.beauty.core.interceptor;

import com.chockwa.beauty.exception.BizException;
import com.chockwa.beauty.common.utils.JwtUtils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 14:17
 * @description:
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 需要检验登陆的黑名单
     */
    private static final ImmutableSet<String> NEED_CHECK_LOGIN_URIS = ImmutableSet.<String>builder()
//            .add("/file/oneUpload")
            .add("/source/getSourceDetail")
            .add("/source/delete")
            .add("/source/save").build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(checkNeedLoginOrNot(request.getRequestURI())){
            String token = request.getHeader("beautyT");
            if(StringUtils.isBlank(token) || !JwtUtils.verifyToken(token)){
                throw BizException.TOKEN_EXPIRE;
            }
            UserInfo.set((User) redisUtils.get(token));
        }
        return true;
    }

    private boolean checkNeedLoginOrNot(String uri){
        if(StringUtils.isBlank(uri)){
            throw new RuntimeException("uri is blank");
        }
        return NEED_CHECK_LOGIN_URIS.contains(uri);
    }
}
