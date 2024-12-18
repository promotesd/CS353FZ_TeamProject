package com.project.app.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.basic.entity.SysFile;
import com.project.app.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author admin
 * @since 2024
 */
public interface SystemFileService  {

    List<SysFile> upload(MultipartFile[] files, FileTypeEnum fileTypeEnum);





}
