package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chockwa.beauty.entity.Source;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SourceMapper extends BaseMapper<Source> {
    List<Source> getIndexSource(@Param("pageIndex")Integer pageIndex, @Param("pageSize")Integer pageSize);
}