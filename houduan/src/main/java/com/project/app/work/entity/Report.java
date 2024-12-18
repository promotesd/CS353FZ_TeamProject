package com.project.app.work.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;





/**
 * <p>
 * 举报表
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@Schema( description = "举报表")
public class Report {

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="被举报帖子ID")
    private Integer postId;

    @Schema(description="举报原因")
    private String reason;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description="举报人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;





}
