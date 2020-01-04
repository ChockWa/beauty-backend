package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.constant.QmType;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.QmInfo;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.QmInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    public static final String UID = "91a96c4621974583b987c8b72f2f9ed4";

    @Autowired
    private QmInfoMapper qmInfoMapper;

    public PageResult<QmInfo> selectVideoPage(PageParam pageParam, String content){
        IPage<QmInfo> infoIPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmInfoMapper.selectPage(infoIPage, new QueryWrapper<QmInfo>().lambda()
                .like(StringUtils.isNotBlank(content), QmInfo::getName, content)
                .eq(QmInfo::getStatus, 1)
                .eq(QmInfo::getType, QmType.VIDEO.getCode())
                .orderByDesc(QmInfo::getCreateTime));

        infoIPage.getRecords().forEach(e -> {
            e.setContactCode(null);
            e.setContact(null);
        });
        PageResult<QmInfo> result = new PageResult<>();
        result.setRecords(infoIPage.getRecords());
        result.setTotal(infoIPage.getTotal());
        return result;
    }

    public PageResult<QmInfo> selectVideoMgmtPage(PageParam pageParam){
        IPage<QmInfo> infoIPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmInfoMapper.selectPage(infoIPage, new QueryWrapper<QmInfo>().lambda()
                .eq(QmInfo::getType, QmType.VIDEO.getCode())
                .orderByDesc(QmInfo::getCreateTime));

        infoIPage.getRecords().forEach(e -> {
            if(!UID.equals(UserInfo.get().getUid())){
                e.setContact(null);
                e.setContactCode(null);
            }
        });
        PageResult<QmInfo> result = new PageResult<>();
        result.setRecords(infoIPage.getRecords());
        result.setTotal(infoIPage.getTotal());
        return result;
    }
}
