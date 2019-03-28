package com.chockwa.beauty.common.utils;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 13:45
 * @description:
 */

import com.chockwa.beauty.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
public class JwtUtils {


    public static final String SUBJECT = "onehee";

    // 过期时间，毫秒，一個小時
    public static final long EXPIRE = 1000*60*60;

    // 秘钥
    public static final  String APPSECRET = "onehee666";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String createToken(User user){

        if(user == null || user.getUid() == null || user.getUserName() == null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("uid",user.getUid())
                .claim("userName",user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean verifyToken(String token){
        try{
            final Claims claims =  Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return  claims == null || !SUBJECT.equals(claims.getSubject()) ? false : true;
        }catch (Exception e){
            log.error("校验jwt出错:{}",e.getMessage());
        }
        return false;
    }
}