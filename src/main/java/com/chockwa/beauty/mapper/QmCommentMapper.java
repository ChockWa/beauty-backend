package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chockwa.beauty.dto.QmCommentDto;
import com.chockwa.beauty.entity.QmComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QmCommentMapper extends BaseMapper<QmComment> {

    IPage<QmCommentDto> selectCommentPage(IPage<QmCommentDto> iPage, @Param("qmId")String qmId);
}