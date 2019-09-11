package com.chockwa.beauty.service;

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

}
