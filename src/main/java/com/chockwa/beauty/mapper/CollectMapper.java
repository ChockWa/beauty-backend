package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.CollectDto;
import com.chockwa.beauty.dto.QmCommentDto;
import com.chockwa.beauty.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    List<CollectDto> selectCollectPage(Page<CollectDto> page, @Param("uid")String uid);
}