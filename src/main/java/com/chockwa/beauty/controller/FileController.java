package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.UploadResponse;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("file")
@Slf4j
public class FileController extends BaseController {

    // 預上傳文件路徑
    private static final String PREPARE_UPLOAD_FILE_ROOT_PATH = "/data/files/";
    private static final String QM_PREPARE_UPLOAD_FILE_ROOT_PATH = "/data/qms/";

    @Autowired
    private FileService fileService;

    /**
     * 上传zip
     * @param file
     * @return
     */
//    @PostMapping("uploadZip")
//    public synchronized Result uploadZip(@RequestParam("file")MultipartFile file){
//        File tempDir = getTempDir();
//        fileService.unZip(file, tempDir.getAbsolutePath());
//        List<UploadResponse> uploadResponseList = fileService.upload(tempDir.getAbsolutePath());
//        deleteTempDir(tempDir);
//        return Result.SUCCESS().setData("data", uploadResponseList);
//    }

    /**
     * 上传单文件
     * @param file
     * @return
     */
    @RateLimit(fallback = "fallBack")
    @PostMapping("upload")
    public Result upload(@RequestParam("file")MultipartFile file){
        String fileDirFileName = String.valueOf(System.currentTimeMillis());
        return Result.SUCCESS().setData("path", fileService.upload(file, fileDirFileName));
    }

    /**
     * 创建临时目录
     * @return
     */
//    private File getTempDir(){
//        File directory = new File("");
//        String tempDirPath = directory.getAbsolutePath() + "\\.temp\\";
//        File tempDir = new File(tempDirPath);
//        if(tempDir.exists()){
//            tempDir.delete();
//        }
//        tempDir.mkdirs();
//        return tempDir;
//    }

    /**
     * 递归删除文件
     * @param tempDir
     */
//    private void deleteTempDir(File tempDir){
//        if(tempDir != null){
//            File[] files = tempDir.listFiles();
//            for(File file : files){
//                if(file.isFile()){
//                    file.delete();
//                }else {
//                    deleteTempDir(file);
//                }
//            }
//            tempDir.delete();
//        }
//    }

    @GetMapping("oneUpload")
    public Result oneUpload(String prepareFilePath){
        try {
            fileService.uploadFiles(PREPARE_UPLOAD_FILE_ROOT_PATH + prepareFilePath);
        } catch (Exception e) {
            log.error("一键上传失败", e);
        }
        return Result.SUCCESS();
    }

    @GetMapping("uploadQm")
    public Result uploadQmInfos(String prepareFilePath){
        try {
            fileService.uploadQmInfos(QM_PREPARE_UPLOAD_FILE_ROOT_PATH + prepareFilePath);
        } catch (Exception e) {
            log.error("一键上传失败", e);
        }
        return Result.SUCCESS();
    }
}
