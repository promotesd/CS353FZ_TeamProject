package com.project.app.basic.controller;

import com.project.app.basic.entity.SysFile;
import com.project.app.basic.service.SystemFileService;
import com.project.app.enums.FileTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>

 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "file")
public class SystemFileController {

    private final SystemFileService systemFileService;


    @PostMapping("upload")
    @Schema(description = "upload")
    @Parameters({
            @Parameter(name = "files", description = "文件", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "fileTypeEnum", description = "文件类型", required = true, in = ParameterIn.QUERY)
    })
    public List<SysFile> upload(MultipartFile[] files, FileTypeEnum fileTypeEnum) {
        return systemFileService.upload(files, fileTypeEnum);
    }








}
