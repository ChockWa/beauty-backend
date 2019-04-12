package com.chockwa.beauty.controller;

import com.github.tobato.fastdfs.domain.fdfs.GroupState;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

/**
 * @auther: zhuohuahe
 * @date: 2019/4/12 18:44
 * @description:
 */
@RestController
@RequestMapping("fast")
public class FastdfsClientApplicationTests {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private TrackerClient trackerClient;

    public void contextLoads() {
        List<GroupState> groupStates = trackerClient.listGroups();
        for (GroupState groupState : groupStates) {
            System.out.println(groupState);
        }

    }

    /**
     * 测试文件上传
     */
    @GetMapping("upload")
    public void upload() {

//        try {
//            File file = new File("D:\\Users\\zhuohuahe\\Desktop\\rBIVnVyKJaqEBH8kAAAAABbAceA232.jpg");
//
//            FileInputStream inputStream = new FileInputStream(file);
//            //StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), "jpg", null);
//
//            //fastFileStorageClient.uploadSlaveFile(storePath.getGroup(),storePath.getPath(),inputStream,inputStream.available(),"a_",null);
//            StorePath storePath = fastFileStorageClient.uploadSlaveFile("group1","M00/00/00/wKiAjVlpNjiAK5IHAADGA0F72jo578.jpg",inputStream,inputStream.available(),"a_",null);
//
//            //System.out.println(storePath.getGroup() + " " + storePath.getPath());
//
//            inputStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        File file = new File("D:\\Users\\zhuohuahe\\Desktop\\rBIVnVyKJaqEBH8kAAAAABbAceA232.jpg");
        // 上传并且生成缩略图
        StorePath storePath = null;
        try {
            storePath = fastFileStorageClient.uploadFile(
                    new FileInputStream(file), file.length(), "jpg", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    /**
     * 测试上传缩略图
     */
    public void uploadCrtThumbImage() {
        try {
            File file = new File("d:\\ds.jpg");

            FileInputStream inputStream = new FileInputStream(file);
            // 测试上传 缩略图
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, file.length(), "jpg", null);

            System.out.println(storePath.getGroup() + "  " + storePath.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试文件下载
     */
    public void download() {
        FileOutputStream stream = null;
        try {
            byte[] bytes = fastFileStorageClient.downloadFile("group1", "M00/00/00/wKiAjVlpMfiAagnbAADGA0F72jo134_150x150.jpg", new DownloadByteArray());

            stream = new FileOutputStream("a.jpg");

            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试文件删除
     */
    public void deleteFile(){
        fastFileStorageClient.deleteFile("group1","M00/00/00/wKiAjVlpQVyARpQwAADGA0F72jo566.jpg");
    }
}
