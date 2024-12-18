package com.project.app.work.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;





/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@TableName(value = "user_follow",autoResultMap = true)
@Schema( description = "用户关注表")
public class UserFollow {

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="关注者ID")
    private Integer followerId;

    @Schema(description="被关注者ID")
    private Integer followeeId;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;





}
