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
    private static final String SN_PREPARE_UPLOAD_FILE_ROOT_PATH = "/data/sns/";

    @Autowired
    private FileService fileService;

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

    @GetMapping("uploadQm")
    public Result uploadSnInfos(String prepareFilePath){
        try {
            fileService.uploadQmInfos(SN_PREPARE_UPLOAD_FILE_ROOT_PATH + prepareFilePath);
        } catch (Exception e) {
            log.error("一键上传失败", e);
        }
        return Result.SUCCESS();
    }
}
