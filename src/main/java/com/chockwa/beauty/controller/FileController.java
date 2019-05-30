package com.chockwa.beauty.controller;

import com.chockwa.beauty.dto.UploadResponse;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传zip
     * @param file
     * @return
     */
    @PostMapping("uploadZip")
    public synchronized Result uploadZip(@RequestParam("file")MultipartFile file){
        File tempDir = getTempDir();
        fileService.unZip(file, tempDir.getAbsolutePath());
        List<UploadResponse> uploadResponseList = fileService.upload(tempDir.getAbsolutePath());
        deleteTempDir(tempDir);
        return Result.SUCCESS().setData("data", uploadResponseList);
    }

//    @PostMapping("unZipAndUpload")
//    public synchronized Result unZipAndUpload(@RequestParam("file")MultipartFile file){
//        fileService.unZipAndUpload(file);
//        return Result.SUCCESS();
//    }

    /**
     * 上传单文件
     * @param file
     * @return
     */
    @PostMapping("upload")
    public synchronized Result upload(@RequestParam("file")MultipartFile file){
        return Result.SUCCESS().setData(fileService.upload(file));
    }

    /**
     * 创建临时目录
     * @return
     */
    private File getTempDir(){
        File directory = new File("");
        String tempDirPath = directory.getAbsolutePath() + "\\.temp\\";
        File tempDir = new File(tempDirPath);
        if(tempDir.exists()){
            tempDir.delete();
        }
        tempDir.mkdirs();
        return tempDir;
    }

    /**
     * 递归删除文件
     * @param tempDir
     */
    private void deleteTempDir(File tempDir){
        if(tempDir != null){
            File[] files = tempDir.listFiles();
            for(File file : files){
                if(file.isFile()){
                    file.delete();
                }else {
                    deleteTempDir(file);
                }
            }
            tempDir.delete();
        }
    }

    @GetMapping("oneUpload")
    public Result oneUpload(String prepareFilePath){
        fileService.uploadFiles(prepareFilePath);
        return Result.SUCCESS();
    }
}
