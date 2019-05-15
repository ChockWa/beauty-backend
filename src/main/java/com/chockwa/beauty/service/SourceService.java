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

    public Source getSource(Long sourceId){
        if(sourceId == null){
            return null;
        }
        return sourceMapper.selectById(sourceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSource(AddSourceDto addSourceDto){
        Long sourceId = null;
        String cover = addSourceDto.getSourceDetailList().get(0).getThumbImage();
        addSourceDto.getSource().setCover(cover);
        if(addSourceDto.getSource().getId() == null){
            sourceMapper.insert(addSourceDto.getSource());
            sourceId = addSourceDto.getSource().getId();
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

    public AddSourceDto getSourceDetail(Long sourceId){
        if(sourceId == null){
            return null;
        }
        AddSourceDto addSourceDto = new AddSourceDto();
        addSourceDto.setSource(sourceMapper.selectById(sourceId));
        addSourceDto.setSourceDetailList(sourceDetailMapper.selectList(new QueryWrapper<SourceDetail>().eq("source_id", sourceId)));
        return addSourceDto;
    }
}
