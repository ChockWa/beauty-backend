package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.QmCommentDto;
import com.chockwa.beauty.entity.QmComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QmCommentMapper extends BaseMapper<QmComment> {

    List<QmCommentDto> selectCommentPage(Page<QmCommentDto> page, @Param("qmId")String qmId);
}