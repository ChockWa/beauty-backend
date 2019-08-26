package com.chockwa.beauty.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.*;
import com.chockwa.beauty.mapper.QmBugLogMapper;
import com.chockwa.beauty.mapper.QmCommentMapper;
import com.chockwa.beauty.mapper.QmInfoMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 08:52
 * @description:
 */
@Service
public class QmService {

    @Autowired
    private QmInfoMapper qmInfoMapper;
    @Autowired
    private QmBugLogMapper qmBugLogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QmCommentMapper qmCommentMapper;

    public PageResult<QmInfo> selectQmPage(PageParam pageParam, int area){
        IPage<QmInfo> infoIPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmInfoMapper.selectPage(infoIPage, new QueryWrapper<QmInfo>().lambda()
                .eq(QmInfo::getArea, area)
                .orderByDesc(QmInfo::getCreateTime));
        infoIPage.getRecords().forEach(e -> e.setContact(null));
        PageResult<QmInfo> result = new PageResult<>();
        result.setRecords(infoIPage.getRecords());
        result.setTotal(infoIPage.getTotal());
        return result;
    }

    public QmInfo getQmInfo(String qmId){
        QmInfo qm = qmInfoMapper.selectById(qmId);
        if(qm == null){
            throw new IllegalStateException("QM信息不存在");
        }
        if(UserInfo.get() == null ||
                qmBugLogMapper.selectList(new QueryWrapper<QmBuyLog>().lambda()
                        .eq(QmBuyLog::getUid, UserInfo.get().getUid())
                        .eq(QmBuyLog::getQmId, qmId)).isEmpty()){
            qm.setContact(null);
        }
        return qm;
    }

    @Transactional(rollbackFor = Exception.class)
    public QmInfo bugQmInfo(String qmId){
        QmInfo qmInfo = qmInfoMapper.selectById(qmId);
        if(qmInfo == null){
            throw new IllegalStateException("QM信息不存在");
        }
        User user = userMapper.selectById(UserInfo.get().getUid());
        if(user == null){
            throw new IllegalStateException("用戶不存在");
        }
        if(qmInfo.getPrice() > user.getCoin()){
            throw new IllegalStateException("金幣數不足，請先充值");
        }
        user.setCoin(user.getCoin() - qmInfo.getPrice());
        user.setUpdateTime(new Date());
        userMapper.updateById(user);

        qmBugLogMapper.insert(new QmBuyLog(user.getUid(), qmId, new Date()));
        return qmInfo;
    }

    public void addQm(QmInfo qmInfo){
        qmInfo.setCreateTime(new Date());
        qmInfoMapper.insert(qmInfo);
    }

    public void updateQm(QmInfo qmInfo){
        qmInfoMapper.updateById(qmInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteQm(String qmId){
        qmInfoMapper.deleteById(qmId);
        qmCommentMapper.delete(new UpdateWrapper<QmComment>().lambda().eq(QmComment::getQmId, qmId));
    }

    public static void main(String[] args) {
        // {"area":1,"contact":"QQQQQQQQQQQQ","description":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","name":"XXXXXX","price":5,"score":"8.9"}
        QmInfo qmInfo = new QmInfo();
        qmInfo.setArea(1);
        qmInfo.setName("XXXXXX");
        qmInfo.setContact("QQQQQQQQQQQQ");
        qmInfo.setDescription("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        qmInfo.setPrice(5);
        qmInfo.setScore("8.9");
        System.out.println(JSON.toJSONString(qmInfo));
    }
}
