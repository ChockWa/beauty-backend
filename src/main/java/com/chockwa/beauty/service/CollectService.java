package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.CollectDto;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.Collect;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.exception.BizException;
import com.chockwa.beauty.mapper.CollectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectService {
    @Autowired
    private CollectMapper collectMapper;

    public void add(String qmId){
        val query = new QueryWrapper<Collect>().lambda()
                .eq(Collect::getQmId, qmId)
                .eq(Collect::getUid, UserInfo.get().getUid());
        val exist = collectMapper.selectList(query);
        if(!exist.isEmpty()){
            throw new BizException("已收藏过，请勿重新收藏!");
        }

        Collect collect = new Collect();
        collect.setQmId(qmId);
        collect.setUid(UserInfo.get().getUid());
        collectMapper.insert(collect);
    }

    public void remove(String qmId){
        val query = new QueryWrapper<Collect>().lambda()
                .eq(Collect::getQmId, qmId)
                .eq(Collect::getUid, UserInfo.get().getUid());
        collectMapper.delete(query);
    }

    public PageResult<CollectDto> selectPage(PageParam pageParam){
        Page<CollectDto> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        page.setRecords(collectMapper.selectCollectPage(page, UserInfo.get().getUid()));
        PageResult<CollectDto> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}
