package com.project.app.basic.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@TableName("sys_file")
@Schema( description = "File table")
public class SysFile {

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="Name")
    private String name;

    @Schema(description="MD5")
    private String md5;

    @Schema(description="Path")
    private String path;

    @Schema(description="CreateTime")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description="CreateBy")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;





    @TableField(exist = false)
    private String url;
}
