package com.chockwa.beauty.core.interceptor;

import com.chockwa.beauty.disruptor.LogEventDisruptor;
import com.chockwa.beauty.disruptor.LogEventProducer;
import com.chockwa.beauty.disruptor.LogEventTranslator;
import com.chockwa.beauty.entity.Log;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 14:17
 * @description:
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LogEventDisruptor logEventDisruptor;

    /**
     * 需要检验登陆的黑名单
     */
    private static final ImmutableSet<String> NEED_CHECK_LOGIN_URIS = ImmutableSet.<String>builder()
            // 一鍵上傳
            .add("/file/oneUpload").add("/file/upload").add("/file/uploadQm")
            // 後台資源相關
            .add("/source/getSourceDetail").add("/source/delete").add("/source/save")
            // 門戶相關
            .add("/door/search").add("/door/download")
            // 用戶相關
            .add("/user/sign").add("/user/info").add("/user/users").add("/user/addCoin")
            // QM相關
            .add("/qm/comment").add("/qm/bQm").add("/qm/qmsMgmt").add("/qm/info").add("/qm/info-comment")
            // 卡相關
            .add("/card/charge").add("/card/add").add("/card/delete").add("/card/cards")
            // 授權相關
            .add("/auth/auth")
            // 上傳qm相關
            .add("/qm-c/add").add("/qm-c/del").add("/qm-c/list").add("/qm-c/verify")
            .build();

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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        UserInfo.clear();
        addLog(request);
    }

    private void addLog(HttpServletRequest request){
        LogEventProducer producer = new LogEventProducer(new LogEventTranslator(), logEventDisruptor.getRingBuffer());
        Log log = new Log();
        log.setMethod(request.getRequestURI());
        log.setIp(getIpAddress(request));
        log.setParams(Objects.nonNull(UserInfo.get()) ? String.format("{uid:%s}", UserInfo.get().getUid()) : "");
        log.setCreateTime(new Date());
        producer.recordLog(log);
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
