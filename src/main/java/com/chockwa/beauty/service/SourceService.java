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
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        result.getRecords().forEach(e -> e.setZipDownloadLink(null));
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
            sourceMapper.updateById(addSourceDto.getSource());
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
        sourceMapper.deleteById(sourceId);
        sourceDetailMapper.delete(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
    }

    public AddSourceDto getSourceDetail(String sourceId){
        if(StringUtils.isBlank(sourceId)){
            return null;
        }
        AddSourceDto addSourceDto = new AddSourceDto();
        addSourceDto.setSource(sourceMapper.selectById(sourceId));
        addSourceDto.setSourceDetailList(sourceDetailMapper.selectList(new QueryWrapper<SourceDetail>().eq("source_id", sourceId)));

        return addSourceDto;
    }

    /**
     * 讀取首頁資源列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<Source> getIndexSource(Integer pageIndex, Integer pageSize){
        return sourceMapper.getIndexSource(pageIndex, pageSize);
    }

    /**
     * 获取最热的5条记录
     * @return
     */
    public List<Source> getHotestSourceList(Integer pageIndex, Integer pageSize){
        List<SourceHot> sourceHots = sourceHotMapper.selectList(new QueryWrapper<SourceHot>().lambda().orderByDesc(SourceHot::getCount).last("limit " + pageIndex + "," + pageSize));
        List<String> sourceIds = sourceHots.stream().map(m -> m.getSourceId()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(sourceIds)){
            return getIndexSource(1,10);
        }
        List<Source> hotests = sourceMapper.selectList(new QueryWrapper<Source>().lambda().in(Source::getId, sourceIds));
        hotests.forEach(e -> e.setZipDownloadLink(null));
        return hotests;
    }

    public String getZipDownloadLink(String sourceId){
        if(StringUtils.isBlank(sourceId)){
            return null;
        }
        Source source = sourceMapper.selectById(sourceId);
        if(source == null){
            throw new IllegalStateException("source not exist");
        }
        return source.getZipDownloadLink();
    }

    public PageResult<Source> searchSources(String content, PageParam pageParam){
        if(StringUtils.isBlank(content)){
            return null;
        }
        IPage<Source> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<Source> result = sourceMapper.selectPage(iPage, new QueryWrapper<Source>().lambda().like(Source::getName, content).or().like(Source::getOrg, content));
        PageResult<Source> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        result.getRecords().forEach(e -> e.setZipDownloadLink(null));
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }
}
