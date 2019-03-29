package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
