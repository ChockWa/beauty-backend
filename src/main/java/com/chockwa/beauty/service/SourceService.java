package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.common.utils.UUIDUtils;
import com.chockwa.beauty.dto.AddSourceDto;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.entity.SourceHot;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import com.chockwa.beauty.mapper.SourceHotMapper;
import com.chockwa.beauty.mapper.SourceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SourceHotMapper sourceHotMapper;

    public PageResult<Source> getListPage(PageParam pageParam){
        IPage<Source> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<Source> result = sourceMapper.selectPage(iPage, new QueryWrapper<Source>().lambda().orderByDesc(Source::getCreateTime));
        PageResult<Source> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }

    public Source getSource(String sourceId){
        if(StringUtils.isBlank(sourceId)){
            return null;
        }
        return sourceMapper.selectById(sourceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSource(AddSourceDto addSourceDto){
        String sourceId = null;
        String cover = addSourceDto.getSourceDetailList().get(0).getThumbImage();
        addSourceDto.getSource().setCover(cover);
        if(addSourceDto.getSource().getId() == null){
            addSourceDto.getSource().setId(UUIDUtils.getUuid());
            addSourceDto.getSource().setCreateTime(new Date());
            sourceMapper.insert(addSourceDto.getSource());
            sourceId = addSourceDto.getSource().getId();
        }else {
            sourceId = addSourceDto.getSource().getId();
            sourceMapper.updateByPrimaryKeySelective(addSourceDto.getSource());
            sourceDetailMapper.delete(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, addSourceDto.getSource().getId()));
        }
        if(addSourceDto.getSourceDetailList() != null){
            for(SourceDetail sourceDetail : addSourceDto.getSourceDetailList()){
                sourceDetail.setId(UUIDUtils.getUuid());
                sourceDetail.setSourceId(sourceId);
                sourceDetail.setCreateTime(new Date());
            }
            sourceDetailMapper.insertBatch(addSourceDto.getSourceDetailList());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String sourceId){
        if(StringUtils.isBlank(sourceId)){
            return;
        }
        sourceMapper.deleteByPrimaryKey(sourceId);
        sourceDetailMapper.delete(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
    }

    public AddSourceDto getSourceDetail(String sourceId){
        if(StringUtils.isBlank(sourceId)){
            return null;
        }
        AddSourceDto addSourceDto = new AddSourceDto();
        addSourceDto.setSource(sourceMapper.selectById(sourceId));
        addSourceDto.setSourceDetailList(sourceDetailMapper.selectList(new QueryWrapper<SourceDetail>().eq("source_id", sourceId)));

        // 插入热搜信息
        updateHotInfo(sourceId);

        return addSourceDto;
    }

    private synchronized void updateHotInfo(String sourceId){
        SourceHot sourceHot = sourceHotMapper.selectById(sourceId);
        if(sourceHot == null){
            sourceHot = new SourceHot();
            sourceHot.setSourceId(sourceId);
            sourceHot.setCount(1);
            sourceHotMapper.insert(sourceHot);
        }else{
            sourceHot.setCount(sourceHot.getCount() + 1);
            sourceHotMapper.updateById(sourceHot);
        }
    }

    /**
     * 获取最新的前10条记录
     * @return
     */
    public List<Source> getNewerSourceList(){
        return sourceMapper.selectList(new QueryWrapper<Source>().lambda().orderByDesc(Source::getCreateTime).last("limit 10"));
    }

    /**
     * 获取20-30的记录
     * @return
     */
    public List<Source> getOlderSourceList(){
        return sourceMapper.selectList(new QueryWrapper<Source>().lambda().orderByAsc(Source::getCreateTime).last("limit 20,10"));
    }

    /**
     * 获取最热的5条记录
     * @return
     */
    public List<Source> getHotestSourceList(){
        List<SourceHot> sourceHots = sourceHotMapper.selectList(new QueryWrapper<SourceHot>().lambda().orderByDesc(SourceHot::getCount).last("limit 5"));
        List<String> sourceIds = sourceHots.stream().map(m -> m.getSourceId()).collect(Collectors.toList());
        return sourceMapper.selectList(new QueryWrapper<Source>().lambda().in(Source::getId, sourceIds));
    }
}
