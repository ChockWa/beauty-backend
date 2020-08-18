package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.constant.QmType;
import com.chockwa.beauty.dto.CollectDto;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.*;
import com.chockwa.beauty.exception.BizException;
import com.chockwa.beauty.mapper.QmBugLogMapper;
import com.chockwa.beauty.mapper.QmCommentMapper;
import com.chockwa.beauty.mapper.QmInfoMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 08:52
 * @description:
 */
@Service
public class QmService {

    public static final String UID = "91a96c4621974583b987c8b72f2f9ed4";

    @Value("${dns.api-https}")
    private String DNS_HTTPS;

    @Autowired
    private QmInfoMapper qmInfoMapper;
    @Autowired
    private QmBugLogMapper qmBugLogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QmCommentMapper qmCommentMapper;

    public PageResult<QmInfo> selectQmPage(PageParam pageParam, Integer area, String content, Integer type){
        IPage<QmInfo> infoIPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmInfoMapper.selectPage(infoIPage, new QueryWrapper<QmInfo>().lambda()
                .eq(area != null, QmInfo::getArea, area)
                .like(StringUtils.isNotBlank(content), QmInfo::getName, content)
                .eq(QmInfo::getStatus, 1)
                .eq(QmInfo::getType, type)
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

    public PageResult<QmInfo> selectQmMgmtPage(PageParam pageParam, Integer area){
        IPage<QmInfo> infoIPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmInfoMapper.selectPage(infoIPage, new QueryWrapper<QmInfo>().lambda()
                .eq(area != null, QmInfo::getArea, area)
                .eq(QmInfo::getType, QmType.QM.getCode())
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


    public QmInfo getQmInfo(String qmId){
        QmInfo qm = qmInfoMapper.selectById(qmId);
        if(qm == null){
            throw new BizException("QM信息不存在");
        }
        if(setNullContact(UserInfo.get()==null?null:UserInfo.get().getUid(), qmId)){
            qm.setContact(null);
            qm.setContactCode(null);
        }
        return qm;
    }

    private boolean setNullContact(String uid, String qmId){
        if(StringUtils.isBlank(uid)){
            return true;
        }
        User user = userMapper.selectById(uid);
        boolean isVip  = user.getVipEndTime() == null || new Date().after(user.getVipEndTime()) ? false : true;
        return (!isVip) && (qmBugLogMapper.selectList(new QueryWrapper<QmBuyLog>().lambda()
                .eq(QmBuyLog::getUid, UserInfo.get().getUid())
                .eq(QmBuyLog::getQmId, qmId)).isEmpty());
    }

    @Transactional(rollbackFor = Exception.class)
    public QmInfo bugQmInfo(String qmId){
        User user = userMapper.selectById(UserInfo.get().getUid());
        if(user == null){
            throw new IllegalStateException("用戶不存在");
        }
        QmInfo qmInfo = qmInfoMapper.selectById(qmId);
        if(qmInfo == null){
            throw new IllegalStateException("QM信息不存在");
        }
        // 判断今天领取了没有
//        Date now = new Date();
//        if(user.getLastReceiveTime() == null || !DateUtils.isSameDay(user.getLastReceiveTime(),now)){
//            user.setLastReceiveTime(now);
//            userMapper.updateById(user);
//            return qmInfo;
//        }
        if(qmInfo.getPrice() > user.getCoin()){
            throw BizException.COIN_NOT_ENOUGH;
        }
        user.setCoin(user.getCoin() - qmInfo.getPrice());
        user.setUpdateTime(new Date());
        userMapper.updateById(user);

        qmBugLogMapper.insert(new QmBuyLog(user.getUid(), qmId, new Date()));
        return qmInfo;
    }

    public void addQm(QmInfo qmInfo){
        qmInfo.setCreateTime(new Date());
        qmInfo.setCover(qmInfo.getImage().split(",")[0]);
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

    public List<QmInfo> getNewerQms(){
        Page<QmInfo> page = new Page<>(1,8);
        QueryWrapper<QmInfo> query = new QueryWrapper<>();
        query.lambda().eq(QmInfo::getType, QmType.QM.getCode())
                .eq(QmInfo::getStatus, 1)
                .orderByDesc(QmInfo::getCreateTime);
        qmInfoMapper.selectPage(page, query);
        page.getRecords().forEach(e -> {
            e.setContact(null);
            e.setContactCode(null);
            e.setCover(DNS_HTTPS + e.getCover());
        });
        return page.getRecords();
    }

    public List<QmInfo> getNewerSns(){
        Page<QmInfo> page = new Page<>(1,8);
        QueryWrapper<QmInfo> query = new QueryWrapper<>();
        query.lambda().eq(QmInfo::getType, QmType.SN.getCode())
                .eq(QmInfo::getStatus, 1)
                .orderByDesc(QmInfo::getCreateTime);
        qmInfoMapper.selectPage(page, query);
        page.getRecords().forEach(e -> {
            e.setContact(null);
            e.setContactCode(null);
            e.setCover(DNS_HTTPS + e.getCover());
        });
        return page.getRecords();
    }

    public PageResult<CollectDto> selectBuyPage(PageParam pageParam){
        Page<CollectDto> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        page.setRecords(qmInfoMapper.selectBuyPage(page, UserInfo.get().getUid()));
        PageResult<CollectDto> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}
