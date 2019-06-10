package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.entity.SourceHot;
import com.chockwa.beauty.mapper.SourceDetailMapper;
import com.chockwa.beauty.mapper.SourceHotMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PageResult<SourceDetail> getListPage(String sourceId, PageParam pageParam){
        IPage<SourceDetail> iPage = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        IPage<SourceDetail> result = sourceDetailMapper.selectPage(iPage, new QueryWrapper<SourceDetail>().lambda().eq(SourceDetail::getSourceId, sourceId));
        PageResult<SourceDetail> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
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
            temp.setThumbImage(m.getThumbImage());
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
        return Optional.ofNullable(sourceDetail).map(m -> m.getPicUrl()).orElse(null);
    }
}
