package com.chockwa.beauty.service;

import com.chockwa.beauty.entity.PointLog;
import com.chockwa.beauty.mapper.PointLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/5 15:05
 * @description:
 */
@Service
public class PointLogService {

    private static final int SIGN_POINT = 5;

    @Autowired
    private PointLogMapper pointLogMapper;

    private void signGetPoint(String uid){
        addPointLog(uid, SIGN_POINT, "sign");
    }

    public void addPointLog(String uid, int point, String desc){
        PointLog pointLog = new PointLog();
        pointLog.setUid(uid);
        pointLog.setDescription(desc);
        pointLog.setPoint(point);
        pointLogMapper.insert(pointLog);
    }

    public static void main(String[] args) throws IOException {
//        Class clazz = PointLogService.class.getClass();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Enumeration e = classLoader.getResources("test.properties");
//        Enumeration e = PointLogService.class.getClass().getClassLoader().getResources("test.properties");
        while (e.hasMoreElements()){
            URL url = (URL) e.nextElement();
            UrlResource urlResource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(urlResource);
            Iterator i = properties.entrySet().iterator();
            while (i.hasNext()){
                Map.Entry entry = (Map.Entry) i.next();
                System.out.println(entry.getKey());
            }
        }
    }
}
