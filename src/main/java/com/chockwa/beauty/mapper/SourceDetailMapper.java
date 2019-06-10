package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chockwa.beauty.entity.SourceDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SourceDetailMapper extends BaseMapper<SourceDetail> {
    int insertBatch(List<SourceDetail> list);
}