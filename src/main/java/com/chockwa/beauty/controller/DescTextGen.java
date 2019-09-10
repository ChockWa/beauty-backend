package com.chockwa.beauty.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;

import java.io.File;

public class DescTextGen {

    public static void main(String[] args) {
        File file = new File("E:\\已上传\\201907213");
        File[] files = file.listFiles();
        for(File f : files){
            if(!f.isDirectory()){
                continue;
            }
            File[] fs = f.listFiles();
            for(File f1 : fs){
                if(f1.getName().endsWith("txt") || f1.getName().endsWith("url")){
                    f1.delete();
                }
            }
            File descTxt = new File(f.getAbsolutePath() + "\\" + "1.txt");
            String descStr = genDescText(2,1,f.getName());
            FileUtil.writeBytes(descStr.getBytes(), descTxt);
        }
    }

    /**
     *
     * @param category 2-sexy3-wanghong1-yuanchuang
     * @param type
     * @param name
     * @return
     */
    private static String genDescText(int category, int type, String name){
        Desc desc = new Desc(category, type, name);
        return JSON.toJSONString(desc);
    }

    static class Desc{
        int category;
        int type;
        String name;

        public Desc(int category, int type, String name) {
            this.category = category;
            this.type = type;
            this.name = name;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
