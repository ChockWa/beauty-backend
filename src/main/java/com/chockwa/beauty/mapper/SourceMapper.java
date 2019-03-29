package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chockwa.beauty.entity.Source;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SourceMapper extends BaseMapper<Source> {
    int deleteByPrimaryKey(Long id);

    long insertSelective(Source record);

    Source selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Source record);

    int updateByPrimaryKey(Source record);
}