package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.Card;
import com.chockwa.beauty.entity.ChargeLog;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.mapper.CardMapper;
import com.chockwa.beauty.mapper.ChargeLogMapper;
import com.chockwa.beauty.mapper.UserMapper;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class CardService {
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChargeLogMapper chargeLogMapper;

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
        if(type == 0){
            user.setCoin(user.getCoin() + 18);
        }else if(type == 1){
            user.setCoin(user.getCoin() + 30);
        }else if(type == 2){
            user.setCoin(user.getCoin() + 50);
        }else if(type == 3){
            user.setVipEndTime(DateUtils.addMonths(new Date(), 1));
        }else if(type == 4){
            user.setVipEndTime(DateUtils.addMonths(new Date(), 3));
        }else if(type == 5){ // 永久
            user.setVipEndTime(DateUtils.addMonths(new Date(), 24000));
        }
        userMapper.updateById(user);

        card.setStatus(0);
        card.setUseTime(new Date());
        card.setUid(user.getUid());
        cardMapper.updateById(card);

        ChargeLog chargeLog = new ChargeLog();
        chargeLog.setChargeCode(cardNo);
        chargeLog.setUid(user.getUid());
        chargeLog.setCreateTime(new Date());
        chargeLogMapper.insert(chargeLog);
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

    @Transactional(rollbackFor = Exception.class)
    public void genCard(Integer type, Integer count) throws IOException {
        List<String> cardNos = new ArrayList<>(count);
        for(int i=0;i<count;i++){
            Card card = new Card();
            card.setStatus(1);
            String cardNo = getGUID();
            card.setCardNo(cardNo);
            card.setCreateTime(new Date());
            card.setType(type);
            cardMapper.insert(card);
            cardNos.add(cardNo);
        }
        recordCardNos(cardNos);
    }

    private void recordCardNos(List<String> cardNos) throws IOException {
        File file = new File("F:\\cardNo.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(String cardNo : cardNos){
            writer.write(cardNo);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

//    public static void main(String[] args) throws IOException {
//        File file = new File("F:\\cardNo.txt");
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//        String s1 = "qwertyu";
//        String s2 = "1234567";
//        String s3 = "5678900";
//        writer.write(s1);
//        writer.newLine();
//        writer.write(s2);
//        writer.newLine();
//        writer.write(s3);
//        writer.flush();
//        writer.close();
//    }

    private String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 12; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

}
