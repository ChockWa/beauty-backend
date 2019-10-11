package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.dto.SourceDetailInfo;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.entity.SourceHot;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import com.chockwa.beauty.mapper.SourceHotMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:43
 * @description:
 */
@Service
public class SourceDetailService {

    final transient ReentrantLock lock = new ReentrantLock();

    @Autowired
    private SourceDetailMapper sourceDetailMapper;

    @Autowired
    private SourceHotMapper sourceHotMapper;

    @Value("${dns.api-https}")
    private String DNS_HTTPS;

    public PageResult<SourceDetail> getListPage(String sourceId, PageParam pageParam){
        IPage<SourceDetail> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<SourceDetail> result = sourceDetailMapper.selectPage(iPage, new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        if(!CollectionUtils.isEmpty(result.getRecords())){
            result.getRecords().forEach(e -> e.setThumbImage(DNS_HTTPS + e.getThumbImage()));
        }
        PageResult<SourceDetail> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }

    public List<SourceDetailInfo> getList(String sourceId){
        List<SourceDetail> list = sourceDetailMapper.selectList(new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        List<SourceDetailInfo> infos = null;
        if(!CollectionUtils.isEmpty(list)){
            infos = new ArrayList<>(list.size());
            for(int i=0;i<list.size();i++){
                SourceDetailInfo info = new SourceDetailInfo();
                info.setId(i);
                info.setThumbImage(DNS_HTTPS + list.get(i).getThumbImage());
                info.setPicUrl(DNS_HTTPS + list.get(i).getPicUrl());
                infos.add(info);
            }
        }
        return infos;
    }

    public void delete(String sourceDetailId){
        if(StringUtils.isBlank(sourceDetailId)){
            return;
        }
        sourceDetailMapper.deleteById(sourceDetailId);
    }

    public PageResult<SourceDetail> getSourceThumbs(String sourceId, PageParam pageParam){
        IPage<SourceDetail> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<SourceDetail> result = sourceDetailMapper.selectPage(iPage, new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        PageResult<SourceDetail> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords().stream().map(m -> {
            SourceDetail temp = new SourceDetail();
            temp.setId(m.getId());
            temp.setThumbImage(DNS_HTTPS + m.getThumbImage());
            temp.setSourceId(m.getSourceId());
            return temp;
        }).collect(Collectors.toList()));

        // 插入热搜信息
        updateHotInfo(sourceId);

        return pageResult;
    }

    private void updateHotInfo(String sourceId){
        SourceHot sourceHot = sourceHotMapper.selectById(sourceId);
        if(sourceHot == null){
            sourceHot = new SourceHot();
            sourceHot.setSourceId(sourceId);
            sourceHot.setCount(1);
            sourceHotMapper.insert(sourceHot);
        }else{
            sourceHot.setCount(sourceHot.getCount() + 1);
            final Lock lock = this.lock;
            lock.lock();
            try{
                sourceHotMapper.updateById(sourceHot);
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 获取大图
     * @param id
     * @return
     */
    public String getMaxImageById(String id){
        if(StringUtils.isBlank(id)){
            return null;
        }
        SourceDetail sourceDetail = sourceDetailMapper.selectById(id);
        return Optional.ofNullable(sourceDetail).map(m -> DNS_HTTPS + m.getPicUrl()).orElse(null);
    }
}
