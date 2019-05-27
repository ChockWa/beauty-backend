package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:43
 * @description:
 */
@Service
public class SourceDetailService {

    @Autowired
    private SourceDetailMapper sourceDetailMapper;

    public PageResult<SourceDetail> getListPage(Long sourceId, PageParam pageParam){
        IPage<SourceDetail> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<SourceDetail> result = sourceDetailMapper.selectPage(iPage, new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        PageResult<SourceDetail> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }

    public void delete(Long sourceDetailId){
        if(sourceDetailId == null){
            return;
        }
        sourceDetailMapper.deleteByPrimaryKey(sourceDetailId);
    }

    public PageResult<SourceDetail> getSourceThumbs(Long sourceId, PageParam pageParam){
        IPage<SourceDetail> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<SourceDetail> result = sourceDetailMapper.selectPage(iPage, new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        PageResult<SourceDetail> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords().stream().map(m -> {
            SourceDetail temp = new SourceDetail();
            temp.setId(m.getId());
            temp.setThumbImage(m.getThumbImage());
            return temp;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    /**
     * 获取大图
     * @param id
     * @return
     */
    public String getMaxImageById(Long id){
        if(null == id){
            return null;
        }
        SourceDetail sourceDetail = sourceDetailMapper.selectById(id);
        return Optional.ofNullable(sourceDetail).map(m -> m.getPicUrl()).orElse(null);
    }
}
