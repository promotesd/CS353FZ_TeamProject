package com.project.app.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Getter
@Setter
@TableName(autoResultMap = true)
@Schema(description = "用户表")
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements UserDetails {


    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="用户名")
    private String username;

    @Schema(description="密码")
    private String password;

    @Schema(description="邮箱")
    private String email;

    @Schema(description="昵称")
    private String nickname;

    @Schema(description="头像")
    private String avatar;

    @Schema(description="简介")
    private String bio;

    @Schema(description="角色")
    private String role;

    @Schema(description="发帖数")
    private Long postCount;

    @Schema(description="回复数")
    private Long replyCount;

    @Schema(description="关注者数")
    private Long followerCount;

    @Schema(description="关注数")
    private Long followingCount;


    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description="创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;



    @TableField(exist = false)
    private List<String> authorityList = new ArrayList<>();


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authoritiesRole = Stream.of(getRole()).map(SimpleGrantedAuthority::new).toList();
        List<SimpleGrantedAuthority> authorities = authorityList.stream().map(SimpleGrantedAuthority::new).toList();
        List<SimpleGrantedAuthority> role = Stream.of(getRole()).map(s -> new SimpleGrantedAuthority("ROLE_" + s)).collect(Collectors.toList());
        role.addAll(authorities);
        role.addAll(authoritiesRole);
        return role;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
