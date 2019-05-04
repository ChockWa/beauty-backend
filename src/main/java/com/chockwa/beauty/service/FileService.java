package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.chockwa.beauty.common.utils.InputStreamCacher;
import com.chockwa.beauty.common.utils.ZipUtils;
import com.chockwa.beauty.dto.UploadResponse;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FileService {

    @Value("${thumb-image.width}")
    private int thumbWidth;

    @Value("${thumb-image.height}")
    private int thumbHeight;

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
            return new UploadResponse(file.getOriginalFilename(), "http://beauties.org/"+storePath.getFullPath(), storePath.getFullPath(), null, null);
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        return null;
    }

    public void unZip(MultipartFile file, String tempDirPath){
        try {
            File tempFile = new File(tempDirPath + "\\" +file.getOriginalFilename());
            FileCopyUtils.copy(file.getBytes(), tempFile);
            ZipUtils.unZip(tempFile, tempDirPath);
            tempFile.delete();
        } catch (IOException e) {
            log.error("解压失败", e);
        }
    }

    public List<UploadResponse> upload(String tempDirPath) {
        List<UploadResponse> uploadResponses = new ArrayList<>();
        InputStream inputStream = null;
        InputStreamCacher cacher = null;
        try {
            File fileDir = new File(tempDirPath);
            File[] files = fileDir.listFiles();
            for (File file : files) {
                inputStream = new FileInputStream(file);
                // 缓存文件流
                cacher = new InputStreamCacher(inputStream);
                getThumbImage(cacher, file);
                StorePath thumbStorePath = fastFileStorageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
                StorePath storePath = fastFileStorageClient.uploadFile(cacher.getInputStream(), file.length(), "jpg", null);
                System.out.println(thumbStorePath.getFullPath());
                System.out.println(storePath.getFullPath());
                UploadResponse uploadResponse = new UploadResponse();
                uploadResponse.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
                uploadResponse.setUrl("http://beauties.org/"+storePath.getFullPath());
                uploadResponse.setThumbUrl("http://beauties.org/"+thumbStorePath.getFullPath());
                uploadResponse.setOriginUrl(storePath.getFullPath());
                uploadResponse.setOriginThumbUrl(thumbStorePath.getFullPath());
                uploadResponses.add(uploadResponse);
                inputStream.close();
                cacher.close();
                cacher = null;
            }
            return uploadResponses;
        } catch (Exception e) {
            log.error("上传失败");
            throw new RuntimeException("上传失败", e);
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(cacher != null){
                    cacher.close();
                }
            } catch (IOException e) {
                log.error("关闭流失败", e);
            }
        }
    }

    /**
     * 生成缩略图流
     * @param cacher
     * @return
     */
    public void getThumbImage(InputStreamCacher  cacher, File file){
        ByteArrayOutputStream os = null;
        try {
            BufferedImage originImage = ImageIO.read(cacher.getInputStream());
            int originWidth = originImage.getWidth();
            int originHeight = originImage.getHeight();
            if(originWidth > originHeight){
//                BigDecimal percent = new BigDecimal(originHeight - 1).divide(new BigDecimal(thumbHeight),3,BigDecimal.ROUND_HALF_DOWN);
//                BigDecimal width = new BigDecimal(thumbWidth).multiply(percent).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//                BufferedImage image = Thumbnails.of(cacher.getInputStream())
//                        .sourceRegion(Positions.BOTTOM_LEFT, 1,1)
//                        .size(width.intValue(), originHeight - 1)
//                        .keepAspectRatio(false).asBufferedImage();
//                os = new ByteArrayOutputStream();
//                ImageIO.write(image, "jpg", os);
//                BufferedImage thumbImage = Thumbnails.of(new ByteArrayInputStream(os.toByteArray()))
//                        .size(thumbWidth, thumbHeight)
//                        .asBufferedImage();
//                os.close();
//                os = null;
//                os = new ByteArrayOutputStream();
//                ImageIO.write(thumbImage, "jpg", os);
//                return new ByteArrayInputStream(os.toByteArray());
            }else{
                Thumbnails.of(cacher.getInputStream())
                        .size(thumbWidth, thumbHeight)
                        .keepAspectRatio(false)
                        .toFile(file);
            }
        } catch (IOException e) {
            log.error("生成缩略图失败");
            throw new RuntimeException("生成缩略图失败");
        }finally {
            try {
                if(os != null){
                    os.close();
                }
            }catch (Exception e){
                log.error("关闭流失败", e);
            }
        }
    }

//    public void unZipAndUpload(MultipartFile uploadZip){
//        List<UploadResponse> uploadResponses = new ArrayList<>();
//        InputStream is = null;
//        OutputStream os = null;
//        ZipInputStream zis = null;
//        try {
//            byte[] zipBytes = FileCopyUtils.copyToByteArray(uploadZip.getInputStream());
//            FileImageInputStream fileImageInputStream = new FileImageInputStream()
//            zis = new ZipInputStream(new ByteArrayInputStream(zipBytes));
//            ZipEntry zipEntry = null;
//            java.util.zip.ZipFile.
//            ZipFile zipFile = new ZipFile(new String(zipBytes));
//            Enumeration<?> entries = zipFile.getEntries();
//            int count = 1;
//            while((zipEntry = zis.getNextEntry()) != null) {
//                ZipEntry entry = (ZipEntry)entries.nextElement();
//                String fileFullName = entry.getName();
//                // 检查是否是文件夹
//                if(fileFullName.length() == fileFullName.lastIndexOf("/") + 1){
//                    continue;
//                }
//                is = zipFile.getInputStream(entry);
//                if(zipEntry.isDirectory()){
//                    continue;
//                }
//                byte[] bs = new byte[1024];
//                int length = 0;
//                os = new FileOutputStream("D:\\test\\image_" + count + ".jpg");
//                while ((length=zis.read(bs)) > 0){
//                    os.write(bs, 0, length);
//                }
//                StorePath storePath = fastFileStorageClient.uploadFile(is, entry.getSize(), "jpg", null);
//                System.out.println(storePath.getFullPath());
//                UploadResponse uploadResponse = new UploadResponse();
//                uploadResponse.setName(entry.getName().substring(0, entry.getName().lastIndexOf(".")));
//                uploadResponse.setUrl("http://beauties.org/"+storePath.getFullPath());
//                uploadResponse.setOriginUrl(storePath.getFullPath());
//                uploadResponses.add(uploadResponse);
//                os.close();
//                is.close();
//                count++;
//            }
//        } catch (IOException e) {
//            log.error("解压失败", e);
//        } finally {
//            try{
//                if(zis != null){
//                    zis.close();
//                }
//                if(os != null){
//                    os.close();
//                }
//                if(is != null){
//                    os.close();
//                }
//            }catch (Exception e){
//                log.error("关闭流失败", e);
//            }
//        }
//    }
}
