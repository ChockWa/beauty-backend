package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.AddSourceDto;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import com.chockwa.beauty.mapper.SourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:43
 * @description:
 */
@Service
public class SourceService {

    @Autowired
    private SourceMapper sourceMapper;

    @Autowired
    private SourceDetailMapper sourceDetailMapper;

    public PageResult<Source> getListPage(PageParam pageParam){
        IPage<Source> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<Source> result = sourceMapper.selectPage(iPage, null);
        PageResult<Source> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSource(AddSourceDto addSourceDto){
        Long sourceId = null;
        if(addSourceDto.getSource().getId() == null){
            sourceId = sourceMapper.insertSelective(addSourceDto.getSource());
        }else {
            sourceId = addSourceDto.getSource().getId();
            sourceMapper.updateByPrimaryKeySelective(addSourceDto.getSource());
            sourceDetailMapper.delete(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, addSourceDto.getSource().getId()));
        }
        if(addSourceDto.getSourceDetailList() != null){
            for(SourceDetail sourceDetail : addSourceDto.getSourceDetailList()){
                sourceDetail.setSourceId(sourceId);
            }
            sourceDetailMapper.insertBatch(addSourceDto.getSourceDetailList());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long sourceId){
        if(sourceId == null){
            return;
        }
        sourceMapper.deleteByPrimaryKey(sourceId);
        sourceDetailMapper.delete(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
    }
}
