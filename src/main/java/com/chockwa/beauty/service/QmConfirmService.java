package com.chockwa.beauty.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.QmConfirm;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.QmConfirmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void verify(String qmId, Integer status){
        Assert.notNull(qmId, "QmId不能為空");
        Assert.notNull(status, "審核狀態不能為空");
        QmConfirm qmConfirm = new QmConfirm();
        qmConfirm.setId(qmId);
        qmConfirm.setStatus(status);
        qmConfirmMapper.updateById(qmConfirm);
    }

    public void delete(String qmId){
        qmConfirmMapper.deleteById(qmId);
    }
}
