package com.chockwa.beauty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.CollectDto;
import com.chockwa.beauty.entity.QmInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QmInfoMapper extends BaseMapper<QmInfo> {
    List<CollectDto> selectBuyPage(Page<CollectDto> page, @Param("uid")String uid);
}