package com.project.app.work.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.List;

import com.project.app.basic.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;





/**
 * <p>
 * 帖子表
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@Schema( description = "帖子表")
public class Post {

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="帖子标题")
    private String title;

    @Schema(description="帖子内容")
    private String content;

    @Schema(description="分类（如寻物启事、技能咨询、资源共享等）")
    private String category;

    @Schema(description="图片")
    private String imageUrl;

    @Schema(description="点赞数")
    private Integer likeCount;

    @Schema(description="回复数")
    private Integer replyCount;

    @Schema(description="是否热门")
    private Boolean isHot;

    @Schema(description="是否被举报")
    private Boolean isReported;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description="发布人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    @TableField(exist = false)
    private List<Reply> replies;


    @TableField(exist = false)
    private SysUser user;



}
