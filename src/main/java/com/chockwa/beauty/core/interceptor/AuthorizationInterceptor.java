package com.chockwa.beauty.core.interceptor;

import com.chockwa.beauty.exception.BizException;
import com.chockwa.beauty.common.utils.JwtUtils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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

//    @Autowired
//    private LogEventDisruptor logEventDisruptor;

    /**
     * 需要检验登陆的黑名单
     */
    private static final ImmutableSet<String> NEED_CHECK_LOGIN_URIS = ImmutableSet.<String>builder()
            // 一鍵上傳
            .add("/file/oneUpload")
            // 後台獲取資源詳情
            .add("/source/getSourceDetail")
            // 後台刪除資源
            .add("/source/delete")
            // 後台保存資源
            .add("/source/save")
            // 門戶搜索資源
            .add("/door/search")
            // 門戶下載資源
            .add("/door/download").build();

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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        addLog(request);
    }

    private void addLog(HttpServletRequest request){
//        LogEventProducer producer = new LogEventProducer(new LogEventTranslator(), logEventDisruptor.getRingBuffer());
//        Log log = new Log();
//        log.setMethod(request.getRequestURI());
//        log.setIp(getIpAddress(request));
//        log.setCreateTime(new Date());
//        producer.recordLog(log);
    }

    private boolean checkNeedLoginOrNot(String uri){
        if(StringUtils.isBlank(uri)){
            throw new RuntimeException("uri is blank");
        }
        return NEED_CHECK_LOGIN_URIS.contains(uri);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
