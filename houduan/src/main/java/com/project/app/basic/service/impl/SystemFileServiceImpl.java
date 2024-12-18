package com.project.app.basic.service.impl;

import com.project.app.basic.entity.SysFile;
import com.project.app.basic.service.SystemFileService;
import com.project.app.config.MyConfiguration;
import com.project.app.enums.FileTypeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author admin
 * @since 2024
 */
@Service
public class SystemFileServiceImpl  implements SystemFileService, InitializingBean {


    @Resource
    private MyConfiguration myConfiguration;


    @Resource
    private HttpServletResponse response;


    @Override
    public List<SysFile> upload(MultipartFile[] files, FileTypeEnum fileTypeEnum) {
        if (fileTypeEnum == null) {
            fileTypeEnum = FileTypeEnum.FILE;
        }
        List<SysFile> filesInfo = new ArrayList<>();
        for (MultipartFile file : files) {
            SysFile fileSource = new SysFile();
            try (InputStream in = file.getInputStream()) {
                String md5Hex = DigestUtils.md5DigestAsHex(in);
                String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                fileSource.setPath(fileTypeEnum.toString() + "/" + md5Hex + suffix);
                fileSource.setName(file.getOriginalFilename());
                fileSource.setCreateTime(LocalDateTime.now());
                fileSource.setMd5(md5Hex);
                filesInfo.add(fileSource);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            try (OutputStream out = new FileOutputStream(myConfiguration.getFilePath() + fileSource.getPath())) {
                FileCopyUtils.copy(file.getInputStream(), out);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }



        filesInfo.forEach(s -> s.setUrl("/api"+myConfiguration.getFileAccess() + s.getPath()));

        return filesInfo;


    }









    @Override
    public void afterPropertiesSet() {
        FileTypeEnum[] values = FileTypeEnum.values();
        System.out.println(myConfiguration.getFilePath());
        for (FileTypeEnum value : values) {
            String dirName = value.toString();
            File file = new File(myConfiguration.getFilePath() + dirName);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}
