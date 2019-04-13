package com.chockwa.beauty.controller;

import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("upload")
    public Result upload(@RequestParam("file")MultipartFile file){
        return Result.SUCCESS().setData(fileService.upload(file));
    }
}
