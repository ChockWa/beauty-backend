package com.chockwa.beauty.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @auther: zhuohuahe
 * @date: 2019/5/14 11:05
 * @description:
 */
@Slf4j
@Component
public class ImageUtils {

    public static void createImageFromExist(File fromImagePath, File saveImagePath, Integer width, Integer height){
        if(width == null && height == null){
            throw new IllegalArgumentException("寬和高不能同時為空");
        }
        try {
            if(null == width){
                // 等高縮放
                Thumbnails.of(fromImagePath)
                        .height(height)
                        .keepAspectRatio(true)
                        .toFile(saveImagePath);
            }else if(null == height){
                // 等寬縮放
                Thumbnails.of(fromImagePath)
                        .width(width)
                        .keepAspectRatio(true)
                        .toFile(saveImagePath);
            }else{
                // 按具體高寬縮放
                Thumbnails.of(fromImagePath)
                        .size(width, height)
                        .keepAspectRatio(false)
                        .toFile(saveImagePath);
            }
        } catch (IOException e) {
            log.error("生成图片失败", e);
        }
    }

    public static void cutImageAndGenThumb(File originfile, File thumbFile, Integer thumbWidth, Integer thumbHeight){
        if(originfile == null){
            throw new IllegalArgumentException("文件不能為空");
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(originfile);
            int originHeight = bufferedImage.getHeight();
            int originWidth = bufferedImage.getWidth();
            if(originWidth > originHeight){
                int cutWidth = new BigDecimal(originHeight).multiply(new BigDecimal(thumbWidth)).divide(new BigDecimal(thumbHeight),0, RoundingMode.HALF_UP).intValue();
                Thumbnails.Builder<File> builder = Thumbnails.of(originfile);
                builder.sourceRegion(Positions.TOP_CENTER, cutWidth, originHeight);
                builder.scale(1).toFile(thumbFile);
                createImageFromExist(thumbFile, thumbFile, null, thumbHeight);
            }else{
                createImageFromExist(thumbFile, thumbFile, thumbWidth, thumbHeight);
            }
        } catch (IOException e) {
            log.error("剪裁圖片失敗", e);
        }
    }

    public static void main(String[] args) {
        File file = new File("e:/粤BT87M5-20190508-174150.jpg");
        File file1 = new File("e:/粤BT87M5-20190508-174150_thumb.jpg");
        cutImageAndGenThumb(file,file1,200,300);
    }
}
