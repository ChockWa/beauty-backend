package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chockwa.beauty.common.utils.JwtUtils;
import com.chockwa.beauty.common.utils.MD5Utils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.common.utils.UUIDUtils;
import com.chockwa.beauty.dto.RegisterDto;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 17:16
 * @description:
 */
@Service
public class AuthorizationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    public String login(String userName, String password){
        Assert.notNull(userName, "username cannot be empty");
        Assert.notNull(password, "password cannot be empty");
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserName, userName));
        if(user == null){
            throw new IllegalStateException("用戶不存在");
        }
        checkPassword(password, user.getPassword(), user.getSalt());
        String token = JwtUtils.createToken(user);
        redisUtils.set(token, user);
        return token;
    }

    public String registerAndLogin(RegisterDto registerDto){
        Assert.notNull(registerDto.getUserName(), "Username cannot be empty");
        Assert.notNull(registerDto.getConfirmPassword(), "Confirm that the password cannot be empty");
        Assert.notNull(registerDto.getConfirmPassword(), "Password cannot be empty");
//        Assert.notNull(registerDto.getEmail(), "Email cannot be empty");
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserName, registerDto.getUserName()));
        if(user != null){
            throw new IllegalStateException("用戶名已存在");
        }
        user = new User();
        user.setUid(UUIDUtils.getUuid());
        user.setUserName(registerDto.getUserName());
        String salt = UUIDUtils.getUuid();
        user.setSalt(salt);
        user.setPassword(MD5Utils.md5(salt + registerDto.getPassword() + salt));
        user.setEmail(registerDto.getEmail());
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsVip(0);
        userMapper.insert(user);

        return login(user.getUserName(), registerDto.getPassword());
    }

    private void checkPassword(String password, String md5Password, String salt){
        if(!md5Password.equals(MD5Utils.md5(salt + password + salt))){
            throw new IllegalStateException("密碼錯誤");
        }
    }

    public void checkVerifyCode(String uuid, String verifyCode){
        if(StringUtils.isBlank(uuid) || StringUtils.isBlank(verifyCode)){
            throw new IllegalArgumentException("參數有誤");
        }
        if(!verifyCode.equalsIgnoreCase((String) redisUtils.get(uuid))){
            throw new IllegalStateException("驗證碼錯誤");
        }
    }

    public static void main(String[] args) {
        String salt = UUIDUtils.getUuid();
        System.out.println(salt);
        System.out.println(MD5Utils.md5(salt + "123456" + salt));
    }
}
