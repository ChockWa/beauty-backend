package com.chockwa.beauty.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.dto.QmCommentDto;
import com.chockwa.beauty.entity.QmComment;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.QmCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 09:40
 * @description:
 */
@Service
public class CommentService {

    @Autowired
    private QmCommentMapper qmCommentMapper;

    public void qmComment(String qmId, String comment){
        QueryWrapper<QmComment> query = new QueryWrapper<>();
        query.lambda().eq(QmComment::getUid, UserInfo.get().getUid()).orderByDesc(QmComment::getCreateTime);
        QmComment lastComment = qmCommentMapper.selectList(query).get(0);
        if(Objects.nonNull(lastComment)){
            if(DateUtil.between(new Date(), lastComment.getCreateTime(), DateUnit.MINUTE) < 3){
                throw new IllegalStateException("兩次評論要相隔3分鐘，請稍後再試");
            }
        }
        QmComment qmComment = new QmComment();
        qmComment.setQmId(qmId);
        qmComment.setComment(comment);
        qmComment.setUid(UserInfo.get().getUid());
        qmComment.setCreateTime(new Date());
        qmCommentMapper.insert(qmComment);
    }

    public PageResult<QmCommentDto> selectCommentPage(String qmId, PageParam pageParam){
        IPage<QmCommentDto> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        qmCommentMapper.selectCommentPage(iPage, qmId);
        PageResult<QmCommentDto> result = new PageResult<>();
        result.setTotal(iPage.getTotal());
        result.setRecords(iPage.getRecords());
        return result;
    }
}
