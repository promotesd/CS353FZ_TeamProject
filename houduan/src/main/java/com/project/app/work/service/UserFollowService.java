package com.project.app.work.service;

import com.project.app.basic.entity.SysUser;
import com.project.app.work.entity.UserFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * <p>
 * 用户关注表 服务类
 * </p>
 *
 * @author admin
 * @since 2024
 */
public interface UserFollowService extends IService<UserFollow> {
        Page<UserFollow> page(PageDTO pageDTO);
        List<UserFollow> listAll(QueryDTO queryDTO);
        UserFollow getUserFollowById(Integer id);
        boolean deleteUserFollowById(Integer id);
        boolean deleteUserFollowByIds(List<Integer> ids);
        boolean addUserFollow(UserFollow obj);
        boolean updateUserFollowById(UserFollow obj);

        boolean follow(UserFollow obj);

        boolean followed(UserFollow obj);

        List<SysUser> fansList();
        List<SysUser> floowList();
}
