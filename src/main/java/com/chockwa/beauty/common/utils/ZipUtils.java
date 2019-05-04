package com.chockwa.beauty.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * @auther: zhuohuahe
 * @date: 2019/4/15 10:02
 * @description:
 */
@Slf4j
public class ZipUtils {

    private static final int buffer = 2048;

    /**
     * 解压Zip文件
     * @param file 文件
     */
    public static void unZip(File file, String unZipPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.getEntries();
            while(entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String fileFullName = entry.getName();
                // 检查是否是文件夹
                if(fileFullName.length() == fileFullName.lastIndexOf("/") + 1){
                    continue;
                }
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(unZipPath + fileFullName.substring(fileFullName.lastIndexOf("/")));
                bos = new BufferedOutputStream(fos, buffer);

                int count = 0;
                byte buf[] = new byte[buffer];
                while((count = is.read(buf)) > 0) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();
                is.close();
            }
        }catch(Exception e){
            log.error("解压失败", e);
            throw new RuntimeException("解压失败");
        }finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null) {
                    fos.close();
                }
                if(is != null){
                    is.close();
                }
                if(zipFile != null){
                    zipFile.close();
                }
            }catch(Exception e) {
                log.error("关闭流失败", e);
            }
        }
    }

//    public static void main(String[] args) throws IOException {
//        File directory = new File("");//参数为空
//        String courseFile = directory.getAbsolutePath() + "\\.temp\\";//标准的路径 ;
//        System.out.println(courseFile);
//        unZip(new File("D:\\Users\\zhuohuahe\\Desktop\\Desktop.zip"), courseFile);
//    }
}
