package com.chockwa.beauty.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.chockwa.beauty.common.utils.ImageUtils;
import com.chockwa.beauty.common.utils.UUIDUtils;
import com.chockwa.beauty.common.utils.ZipUtils;
import com.chockwa.beauty.dto.AddSourceDto;
import com.chockwa.beauty.dto.UploadResponse;
import com.chockwa.beauty.dto.UploadResult;
import com.chockwa.beauty.entity.QmInfo;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.entity.SourceDetail;
import com.chockwa.beauty.mapper.QmInfoMapper;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    private static final String UPLOAD_FILE_ROOT_PATH = "/usr/local/go-fastdfs/files/";

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

    @Autowired
    private QmInfoMapper qmInfoMapper;

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
            Source source = expainDescFile(descFile, Source.class);
            String sourceDirName = String.valueOf(System.currentTimeMillis());
            List<SourceDetail> sourceDetails = uploadFiles(sourceDirName, files);
            taskExecutor.execute(() -> genZip(UPLOAD_FILE_ROOT_PATH + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()), sourceDirName, sourceDirName + "/origin"));
            source.setZipDownloadLink(DNS_HTTPS + "/zip/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + sourceDirName + ".zip");
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

    public void genZip(String zipFilePath, String zipName, String targetDirName){
        try {
            ZipUtils.generationZipLinux(zipFilePath, zipName, targetDirName);
        } catch (Exception e) {
            log.error("打包失败", e);
        }
    }

    private <T> T expainDescFile(File descFile, Class<T> clazz){
        try {
            String descJson = FileUtils.readFileToString(descFile, "UTF-8");
            return JSON.parseObject(descJson, clazz);
        } catch (IOException e) {
            log.error("转换失败", e);
        }
        return null;
    }

    private List<SourceDetail> uploadFiles(String fileDirName, File[] files) throws IOException {
        List<SourceDetail> sourceDetails = new ArrayList<>();
        File[] tempFiles = Arrays.copyOf(files, files.length);
        for(File file : tempFiles){
            if(file.getName().contains(".txt")){
                continue;
            }
            sourceDetails.add(upload(fileDirName, file));
        }
        // 生成描述文件并上传
        File descFile = generateDescText(tempFiles[0].getAbsolutePath().substring(0, tempFiles[0].getAbsolutePath().lastIndexOf("/")+1));
        uploadDescText(fileDirName, descFile);
        return sourceDetails;
    }

    private SourceDetail upload(String fileDirName, File file){
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("file", file);
            paramMap.put("output","json");
            paramMap.put("path", "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + fileDirName + "/origin");
            paramMap.put("scene","image");
            String result= HttpUtil.post(DNS_HTTP + ":8080/upload", paramMap);
            UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);
            log.info("uploadResult:{}", JSON.toJSON(uploadResult));

            String thumbFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            String thumbFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/") + 1) + "thumb_" + thumbFileName + ".jpg";
            File thumbFile = new File(thumbFilePath);
            FileUtils.copyFile(file, thumbFile);
            // 生成缩略图
            ImageUtils.cutImageAndGenThumb(thumbFile, thumbFile, 210, 300);
            paramMap.put("file", thumbFile);
            paramMap.put("output","json");
            paramMap.put("path", "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + fileDirName + "/thumb");
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

    public static void main(String[] args) throws IOException {
//        generateDescText("E://");
    }

    private File generateDescText(String targetFilePath) throws IOException {
        File file = new File(targetFilePath + "www.24beauties.xyz.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileUtils.writeByteArrayToFile(file, new String(new String("福利站點，收藏不迷路。" + DNS_HTTPS).getBytes(), Charset.defaultCharset()).getBytes());
        return file;
    }

    private void uploadDescText(String fileDirName, File descText){
        if(descText == null){
            return;
        }
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("file", descText);
        paramMap.put("output","json");
        paramMap.put("path", "/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + fileDirName + "/origin");
        paramMap.put("scene","");
        String result= HttpUtil.post(DNS_HTTP + ":8080/upload", paramMap);
    }

    private SourceDetail genSourceDetail(File file, UploadResult result, UploadResult thumbResult){
        SourceDetail detail = new SourceDetail();
        detail.setThumbImage(thumbResult.getPath());
        detail.setPicUrl(result.getPath());
        detail.setOriginThumbImage(thumbResult.getMd5());
        detail.setOriginUrl(result.getMd5());
        detail.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
        detail.setCreateTime(new Date());
        return detail;
    }

    public static void delete(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("md5", "d41d8cd98f00b204e9800998ecf8427e");
        paramMap.put("output","json");
        String thumbResult= HttpUtil.post("http://198.252.105.138:8080/group/delete", paramMap);
        System.out.println(thumbResult);
    }

    public String upload(File file, String fileDirName){
        HashMap<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("file", file);
        paramMap.put("output","json");
        paramMap.put("path", "/qm/" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "/" + fileDirName);
        paramMap.put("scene","image");
        String result= HttpUtil.post(DNS_HTTP + ":8080/upload", paramMap);
        UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);
        log.info("uploadResult:{}", JSON.toJSON(uploadResult));
        return uploadResult.getPath();
    }

    public void uploadQmInfos(String prepareFilePath){
        File sourceDir = new File(prepareFilePath);
        File[] files = sourceDir.listFiles();
        List<QmInfo> qmInfos = new ArrayList<>();
        List<String> imageUrls = Lists.newArrayList();
        for(File file : files){
            File[] qmFiles = file.listFiles();
            File descFile = Arrays.asList(qmFiles).stream().filter(f -> f.getName().contains(".txt")).findFirst().orElse(null);
            if(descFile == null){continue;}
            QmInfo qmInfo = expainDescFile(descFile, QmInfo.class);
            for(File qmFile : qmFiles){
                String fileDirFileName = String.valueOf(System.currentTimeMillis());
                if(qmFile.getName().contains(".txt")){
                    continue;
                }
                imageUrls.add(upload(qmFile, fileDirFileName));
            }
            qmInfo.setImage(imageUrls.stream().collect(Collectors.joining(",")));
            qmInfo.setCreateTime(new Date());
            qmInfos.add(qmInfo);
        }
        for(QmInfo qmInfo : qmInfos){
            qmInfoMapper.insert(qmInfo);
        }
    }
}
