package com.chockwa.beauty.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.QmConfirm;
import com.chockwa.beauty.entity.QmInfo;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.QmConfirmMapper;
import com.chockwa.beauty.mapper.QmInfoMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/9/17 09:28
 * @description:
 */
@Service
public class QmConfirmService {

    @Autowired
    private QmConfirmMapper qmConfirmMapper;
    @Autowired
    private QmInfoMapper qmInfoMapper;
    @Autowired
    private UserMapper userMapper;

    public void add(QmConfirm qmConfirm){
        Assert.notNull(qmConfirm, "QM信息不能為空");
        Assert.notNull(qmConfirm.getContact().trim(), "QM聯繫不能為空");
        Assert.notNull(qmConfirm.getImage().trim(), "QM照片信息不能為空");
        Assert.notNull(qmConfirm.getArea(), "QM區域信息不能為空");
        Assert.notNull(qmConfirm.getName().trim(), "QM名稱不能為空");
        Assert.notNull(qmConfirm.getDescription().trim(), "QM描述不能為空");
        qmConfirm.setCover(qmConfirm.getImage().split(",")[0]);
        qmConfirm.setStatus(0);
        qmConfirm.setCreateTime(new Date());
        qmConfirm.setUid(UserInfo.get().getUid());
        qmConfirmMapper.insert(qmConfirm);
    }

    public PageResult<QmConfirm> getListPage(PageParam pageParam, Integer status){
        Page<QmConfirm> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmConfirmMapper.selectPage(page, new QueryWrapper<QmConfirm>().lambda().eq(status != null, QmConfirm::getStatus, status));
        PageResult<QmConfirm> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void verify(String qmId, Integer status, Integer price){
        Assert.notNull(qmId, "QmId不能為空");
        Assert.notNull(status, "審核狀態不能為空");
        QmConfirm qmConfirm = qmConfirmMapper.selectById(qmId);
        qmConfirm.setStatus(status);
        qmConfirmMapper.updateById(qmConfirm);

        if(1 == status){
            QmInfo qmInfo = new QmInfo();
            qmInfo.setCover(qmConfirm.getCover());
            qmInfo.setContact(qmConfirm.getContact());
            qmInfo.setDescription(qmInfo.getDescription());
            qmInfo.setName(qmInfo.getName());
            qmInfo.setArea(qmConfirm.getArea());
            qmInfo.setScore("9.2");
            qmInfo.setPrice(price);
            qmInfo.setCreateTime(new Date());
            qmInfo.setImage(qmConfirm.getImage());
            qmInfo.setId(qmConfirm.getId());
            qmInfoMapper.insert(qmInfo);

            User user = userMapper.selectById(qmConfirm.getUid());
            user.setCoin(user.getCoin()+15);
            userMapper.updateById(user);
        }
    }

    public void delete(String qmId){
        qmConfirmMapper.deleteById(qmId);
    }
}
