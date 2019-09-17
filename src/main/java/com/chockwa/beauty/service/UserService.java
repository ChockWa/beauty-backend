package com.chockwa.beauty.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.SignLog;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.SignLogMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
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
        user.setCoin(user.getCoin() + SIGN_COIN_DEFAULT);
        user.setLastSignTime(now);
        userMapper.updateById(user);

        SignLog signLog = new SignLog();
        signLog.setUid(UserInfo.get().getUid());
        signLog.setCreateTime(new Date());
        signLogMapper.insert(signLog);
    }

    public Map<String, Object> getUser(){
        User user = userMapper.selectById(UserInfo.get().getUid());
        Map<String, Object> userInfo = BeanUtil.beanToMap(user);
        userInfo.put("isVip", new Date().before(user.getVipEndTime()));
        userInfo.put("isSign", Objects.nonNull(user.getLastSignTime()) ? DateUtil.isSameDay(new Date(), user.getLastSignTime()) : false);
        return userInfo;
    }

    public void addCoin(String uid, Integer coin){
        if(StringUtils.isBlank(uid) || coin == null){
            throw new IllegalArgumentException("參數有誤");
        }
        User user = userMapper.selectById(uid);
        if(user == null){
            throw new IllegalStateException("用戶不存在");
        }
        user.setCoin(user.getCoin() + coin);
        userMapper.updateById(user);
    }

    public PageResult<User> getUserListPage(String userName, PageParam pageParam){
        Page<User> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        userMapper.selectPage(page, new QueryWrapper<User>().lambda().like(StringUtils.isNotBlank(userName), User::getUserName, userName));
        PageResult<User> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}
