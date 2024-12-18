package com.project.app.work.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.project.app.basic.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;





/**
 * <p>
 * 回复表
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@Schema( description = "回复表")
public class Reply {

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="帖子ID")
    private Integer postId;

    @Schema(description="回复内容")
    private String content;

    @Schema(description="父回复ID（支持嵌套回复）")
    private Integer parentId;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description="回复人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    @TableField(exist = false)
    private SysUser sysUser;




}
