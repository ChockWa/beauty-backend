package com.chockwa.beauty.service;

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
