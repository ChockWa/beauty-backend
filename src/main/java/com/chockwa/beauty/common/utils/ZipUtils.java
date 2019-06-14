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


    public static void generationZipLinux(String zipFilePath, String zipName, String targetDirName) throws IOException, InterruptedException {
        File zipFileDir = new File(zipFilePath);
        if (!zipFileDir.exists()) {
            zipFileDir.mkdirs();
        }
        File wd = new File("/bin");
        Process proc = null;
        String enterTempDir = "cd " + zipFileDir;
        String zip = "zip -qr " + zipName + ".zip" + " ./" + targetDirName;
        log.info("zip localtion:{}", zipFileDir);
        try {
            proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
        } catch (IOException e) {
            log.error("zip fail", e);
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println(enterTempDir);
            out.println(zip);
            out.println("exit");
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();
            } catch (Exception e) {
                log.error("打zip包失败", e);
                // 打包失败删除文件
                File tempZip = new File(zipFilePath + "/" + zipName + ".zip");
                if (tempZip.exists()) {
                    tempZip.delete();
                }
                throw e;
            }
        }
    }
}
