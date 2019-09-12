package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.Card;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.CardMapper;
import com.chockwa.beauty.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CardService {
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void useCard(String cardNo, Integer type){
        if(StringUtils.isBlank(cardNo) || type == null){
            throw new IllegalArgumentException("參數有誤");
        }
        Card card = cardMapper.selectById(cardNo);
        if(card == null || 0 == card.getStatus()){
            throw new IllegalStateException("卡密不存在或已失效");
        }
        if(!type.equals(card.getType())){
            throw new IllegalStateException("卡密所屬產品有誤，請重新選擇產品");
        }

        User user = userMapper.selectById(UserInfo.get().getUid());
        if(type == 1){
            user.setCoin(user.getCoin() + 30);
        }else if(type == 2){
            user.setCoin(user.getCoin() + 50);
        }else if(type == 3){
            user.setVipEndTime(DateUtils.addMonths(new Date(), 1));
        }else if(type == 4){
            user.setVipEndTime(DateUtils.addMonths(new Date(), 3));
        }
        userMapper.updateById(user);

        card.setStatus(0);
        card.setUseTime(new Date());
        card.setUid(user.getUid());
        cardMapper.updateById(card);
    }

    public PageResult<Card> getCardsPage(String cardNo, PageParam pageParam){
        Page<Card> page = new Page<>(pageParam.getPageIndex(), pageParam.getPageSize());
        cardMapper.selectPage(page, new QueryWrapper<Card>().lambda().eq(StringUtils.isNotBlank(cardNo), Card::getCardNo, cardNo).orderByDesc(Card::getCreateTime));
        PageResult<Card> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        return result;
    }

    public void addCard(String cardNo, Integer type){
        if(StringUtils.isBlank(cardNo) || type == null){
            throw new IllegalArgumentException("參數有誤");
        }
        Card card = new Card();
        card.setCardNo(cardNo);
        card.setType(type);
        card.setCreateTime(new Date());
        card.setStatus(1);
        cardMapper.insert(card);
    }

    public void deleteCard(String cardNo){
        cardMapper.deleteById(cardNo);
    }

}
