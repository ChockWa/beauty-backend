package com.chockwa.beauty.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.chockwa.beauty.common.utils.ImageUtils;
import com.chockwa.beauty.common.utils.UUIDUtils;
import com.chockwa.beauty.common.utils.ZipUtils;
import com.chockwa.beauty.dto.AddSourceDto;
import com.chockwa.beauty.dto.UploadResponse;
import com.chockwa.beauty.dto.UploadResult;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.entity.SourceDetail;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class FileService {

    private static final String UPLOAD_FILE_ROOT_PATH = "/files/";

    @Value("${thumb-image.width}")
    private int thumbWidth;

    @Value("${thumb-image.height}")
    private int thumbHeight;

    @Value("${dns.api-http}")
    private String DNS_HTTP;

    @Value("${dns.api-https}")
    private String DNS_HTTPS;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private TrackerClient trackerClient;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private TaskExecutor taskExecutor;

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
            return new UploadResponse(file.getOriginalFilename(), "http://beauties.org/"+storePath.getFullPath(), storePath.getFullPath(), null, null);
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        return null;
    }

    public void unZip(MultipartFile file, String tempDirPath){
        try {
            File tempFile = new File(tempDirPath + "/" +file.getOriginalFilename());
            FileCopyUtils.copy(file.getBytes(), tempFile);
            ZipUtils.unZip(tempFile, tempDirPath);
            tempFile.delete();
        } catch (IOException e) {
            log.error("解压失败", e);
        }
    }

    public List<UploadResponse> upload(String tempDirPath) {
        List<UploadResponse> uploadResponses = new ArrayList<>();
        try{
            File fileDir = new File(tempDirPath);
            File[] files = fileDir.listFiles();
            for (File file : files) {
                StorePath storePath = fastFileStorageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
                // 生成缩略图
                ImageUtils.cutImageAndGenThumb(file, file, thumbWidth, thumbHeight);
                StorePath thumbStorePath = fastFileStorageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
                System.out.println(thumbStorePath.getFullPath());
                System.out.println(storePath.getFullPath());
                UploadResponse uploadResponse = new UploadResponse();
                uploadResponse.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
                uploadResponse.setUrl(DNS_HTTPS + "/" + storePath.getFullPath());
                uploadResponse.setThumbUrl(DNS_HTTPS + "/" + thumbStorePath.getFullPath());
                uploadResponse.setOriginUrl(storePath.getFullPath());
                uploadResponse.setOriginThumbUrl(thumbStorePath.getFullPath());
                uploadResponses.add(uploadResponse);
            }
            return uploadResponses;
        }catch (Exception e) {
            log.error("上传失败");
            throw new RuntimeException("上传失败", e);
        }
    }

    public void uploadFiles(String prepareFilePath) throws IOException, InterruptedException {
        if(StringUtils.isBlank(prepareFilePath)){
            return;
        }
        File prepareFileDir = new File(prepareFilePath);
        if(!prepareFileDir.exists()){
            return;
        }
        File[] filesDirs = prepareFileDir.listFiles();
        if(filesDirs == null || filesDirs.length < 1){
            return;
        }
        List<AddSourceDto> addSourceDtos = new ArrayList<>();
        for(File fileDir : filesDirs){
            File[] files = fileDir.listFiles();
            if(files == null || files.length < 1){
                continue;
            }
            File descFile = Arrays.asList(files).stream().filter(f -> f.getName().contains(".txt")).findFirst().orElse(null);
            if(descFile == null){
                continue;
            }
            Source source = expainDescFile(descFile);
            List<SourceDetail> sourceDetails = uploadFiles(fileDir.getName(), files);
            String zipName = UUIDUtils.getUuid();
            // "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + Math.abs(fileDirName.hashCode())
            genZip(UPLOAD_FILE_ROOT_PATH + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + Math.abs(fileDir.getName().hashCode()), zipName);
            source.setZipDownloadLink(DNS_HTTPS + "/zip/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + Math.abs(fileDir.getName().hashCode()) + "/" + zipName + ".zip");
            source.setCover(sourceDetails.get(0).getThumbImage());
            AddSourceDto addSourceDto = new AddSourceDto();
            addSourceDto.setSource(source);
            addSourceDto.setSourceDetailList(sourceDetails);
            addSourceDtos.add(addSourceDto);
        }
        addSourceDtos.forEach(e -> {
            sourceService.saveSource(e);
        });
    }

    @Async
    public void genZip(String zipFilePath, String zipName){
        try {
            ZipUtils.generationZipLinux(zipFilePath, zipName);
        } catch (Exception e) {
            log.error("打包失败", e);
        }
    }

    private Source expainDescFile(File descFile){
        try {
            String descJson = FileUtils.readFileToString(descFile, "UTF-8");
            return JSON.parseObject(descJson, Source.class);
        } catch (IOException e) {
            log.error("转换失败", e);
        }
        return null;
    }

    private List<SourceDetail> uploadFiles(String fileDirName, File[] files){
        List<SourceDetail> sourceDetails = new ArrayList<>();
        File[] tempFiles = Arrays.copyOf(files, files.length);
        for(File file : tempFiles){
            if(file.getName().contains(".txt")){
                continue;
            }
            sourceDetails.add(upload(fileDirName, file));
        }
        return sourceDetails;
    }

    private SourceDetail upload(String fileDirName, File file){
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            String newFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/") + 1) + UUIDUtils.getUuid() + ".jpg";
            File newFile = new File(newFilePath);
            FileUtils.copyFile(file, newFile);
            paramMap.put("file", newFile);
            paramMap.put("output","json");
            paramMap.put("path", "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + Math.abs(fileDirName.hashCode()));
            paramMap.put("scene","image");
            String result= HttpUtil.post(DNS_HTTP + ":8080/upload", paramMap);
            UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);
            log.info("uploadResult:{}", JSON.toJSON(uploadResult));

            String thumbFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/") + 1) + UUIDUtils.getUuid() + ".jpg";
            File thumbFile = new File(thumbFilePath);
            FileUtils.copyFile(newFile, thumbFile);
            // 生成缩略图
            ImageUtils.cutImageAndGenThumb(thumbFile, thumbFile, 210, 300);
            paramMap.put("file", thumbFile);
            paramMap.put("output","json");
            paramMap.put("path", "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + Math.abs(fileDirName.hashCode()));
            paramMap.put("scene","image");
            String thumbResult= HttpUtil.post(DNS_HTTP + ":8080/upload", paramMap);
            UploadResult thumbUploadResult = JSON.parseObject(thumbResult, UploadResult.class);
            log.info("thumbUploadResult:{}", JSON.toJSON(thumbUploadResult));

            return genSourceDetail(file, uploadResult, thumbUploadResult);
        } catch (IOException e) {
            log.error("上傳失敗", e);
        }
        return null;
    }

    private SourceDetail genSourceDetail(File file, UploadResult result, UploadResult thumbResult){
        SourceDetail detail = new SourceDetail();
        detail.setThumbImage(DNS_HTTPS + thumbResult.getPath());
        detail.setPicUrl(DNS_HTTPS + result.getPath());
        detail.setOriginThumbImage(thumbResult.getMd5());
        detail.setOriginUrl(result.getMd5());
        detail.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
        detail.setCreateTime(new Date());
        return detail;
    }

    public static void main(String[] args) {
        File file = new File("E:/test.jpg");
        delete();
    }

    public static void delete(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("md5", "d41d8cd98f00b204e9800998ecf8427e");
        paramMap.put("output","json");
        String thumbResult= HttpUtil.post("http://198.252.105.138:8080/group/delete", paramMap);
        System.out.println(thumbResult);
    }
}
