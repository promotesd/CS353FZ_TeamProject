package com.project.app.util;

import com.project.app.basic.entity.SysUser;
import com.project.app.basic.service.SysUserService;
import com.project.app.work.entity.Post;
import com.project.app.work.entity.Report;
import com.project.app.work.entity.UserFollow;
import com.project.app.work.service.PostService;
import com.project.app.work.service.ReportService;
import com.project.app.work.service.UserFollowService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private static ConfigurableApplicationContext app;


    public static void setApp(ConfigurableApplicationContext app) {
        SecurityUtil.app = app;
    }

    public static SysUser getCurrentUser() {
        SysUserService userService = app.getBean(SysUserService.class);
        SysUser user = userService.lambdaQuery().eq(SysUser::getUsername, SecurityContextHolder.getContext().getAuthentication().getName()).one();


        ReportService reportService = app.getBean(ReportService.class);
        PostService postService = app.getBean(PostService.class);
        UserFollowService userFollowService = app.getBean(UserFollowService.class);



        Long followerCount = userFollowService.lambdaQuery().eq(UserFollow::getFolloweeId, user.getId()).count();

        Long followerId = userFollowService.lambdaQuery().eq(UserFollow::getFollowerId, user.getId()).count();


        Long replyCount = reportService.lambdaQuery().eq(Report::getCreateBy, user.getUsername()).count();

        Long postCount = postService.lambdaQuery().eq(Post::getCreateBy, user.getUsername()).count();


        user.setFollowerCount(followerCount);
        user.setFollowingCount(followerId);
        user.setReplyCount(replyCount);
        user.setPostCount(postCount);
        userService.updateSysUserById(user);

        user.setPassword(null);
        return user;
    }


    public static SysUser getUserByName(String username) {
        SysUserService userService = app.getBean(SysUserService.class);
        ReportService reportService = app.getBean(ReportService.class);
        PostService postService = app.getBean(PostService.class);
        UserFollowService userFollowService = app.getBean(UserFollowService.class);
        SysUser user = userService.lambdaQuery().eq(SysUser::getUsername, username).one();
        Long followerCount = userFollowService.lambdaQuery().eq(UserFollow::getFolloweeId, user.getId()).count();
        Long followerId = userFollowService.lambdaQuery().eq(UserFollow::getFollowerId, user.getId()).count();
        Long replyCount = reportService.lambdaQuery().eq(Report::getCreateBy, user.getUsername()).count();
        Long postCount = postService.lambdaQuery().eq(Post::getCreateBy, user.getUsername()).count();
        user.setFollowerCount(followerCount);
        user.setFollowingCount(followerId);
        user.setReplyCount(replyCount);
        user.setPostCount(postCount);
        userService.updateSysUserById(user);
        user.setPassword(null);
        return user;
    }

    public static SysUser getUserById(Integer id) {
        SysUserService userService = app.getBean(SysUserService.class);
        SysUser user = userService.lambdaQuery().eq(SysUser::getId, id).one();
        user.setPassword(null);
        return user;
    }
}
