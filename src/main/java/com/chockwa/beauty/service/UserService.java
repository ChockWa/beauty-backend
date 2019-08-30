package com.chockwa.beauty.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.chockwa.beauty.entity.SignLog;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.SignLogMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/23 09:17
 * @description:
 */
@Service
public class UserService {

    private static final int SIGN_COIN_DEFAULT = 1;
    private static final int CONTINUITY_SIGN_COIN_DEFAULT = 2;

    @Autowired
    private SignLogMapper signLogMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void sign(){
        User user = userMapper.selectById(UserInfo.get().getUid());
        Date now = new Date();
        if(Objects.nonNull(user.getLastSignTime()) && DateUtils.isSameDay(now, user.getLastSignTime())){
            throw new IllegalStateException("今天已簽到過，請明天再來");
        }
        if(Objects.nonNull(user.getLastSignTime()) && DateUtils.isSameDay(DateUtils.addDays(user.getLastSignTime(),1), now)){
            user.setSignCount(user.getSignCount() + 1);
            if(user.getSignCount() > 3){
                user.setCoin(user.getCoin() + CONTINUITY_SIGN_COIN_DEFAULT);
            }else{
                user.setCoin(user.getCoin() + SIGN_COIN_DEFAULT);
            }
        }else{
            user.setSignCount(0);
            user.setCoin(user.getCoin() + SIGN_COIN_DEFAULT);
        }
        user.setLastSignTime(now);
        userMapper.updateById(user);

        SignLog signLog = new SignLog();
        signLog.setUid(UserInfo.get().getUid());
        signLog.setCreateTime(new Date());
        signLogMapper.insert(signLog);
    }

    public Map<String, Object> getUser(){
        User user = UserInfo.get();
        user.setPassword(null);
        user.setSalt(null);
        user.setUid(null);
        Map<String, Object> userInfo = BeanUtil.beanToMap(user);
        userInfo.put("isSign", DateUtil.isSameDay(new Date(), user.getLastSignTime()));
        return userInfo;
    }
}
