package com.chockwa.beauty.controller;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileRename {

    public static void main(String[] args) {
        File file = new File("E:\\已上传\\wanghong\\wh201907132");
        File[] files = file.listFiles();
        for(File file1 : files){
            if(file1.isDirectory()){
                String path = file1.getAbsolutePath();
                List<File> originFiles = new ArrayList<>();
                File[] list = file1.listFiles();
                for(int i=0;i<list.length;i++){
                    if(list[i].getName().endsWith("txt")){
                        continue;
                    }
                    File newFile = new File(path + "\\" + String.valueOf(System.currentTimeMillis()).substring(8) + list[i].getName().substring(list[i].getName().lastIndexOf(".")));
                    originFiles.add(list[i]);
                    FileUtil.copyFile(list[i], newFile);
                }
                for(File f : originFiles){
                    if(f.exists()){
                        f.delete();
                    }
                }
            }
        }
    }
}
