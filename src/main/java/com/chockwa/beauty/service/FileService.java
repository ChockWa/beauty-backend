package com.chockwa.beauty.service;

import com.chockwa.beauty.dto.UploadResponse;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private TrackerClient trackerClient;

    public UploadResponse upload(MultipartFile file) {
        // 上传并且生成缩略图
        StorePath storePath = null;
        try {
            System.out.println(file.getOriginalFilename());
            storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), "jpg", null);
            // 带分组的路径
            System.out.println(storePath.getFullPath());
            // 不带分组的路径
            System.out.println(storePath.getPath());
            return new UploadResponse(file.getName(), "http://beauties.org/"+storePath.getFullPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
