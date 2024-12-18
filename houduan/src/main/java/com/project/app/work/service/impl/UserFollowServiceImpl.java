package com.project.app.work.service.impl;

import com.project.app.basic.entity.SysUser;
import com.project.app.util.SecurityUtil;
import com.project.app.work.entity.UserFollow;
import com.project.app.work.mapper.UserFollowMapper;
import com.project.app.work.service.UserFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.stereotype.Service;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {
    @Override
    public Page<UserFollow> page(PageDTO pageDTO) {
        LambdaQueryChainWrapper<UserFollow> wrapper = lambdaQuery()
                .like(StringUtils.hasText(pageDTO.getSearch()), UserFollow::getFollowerId, pageDTO.getSearch())
                .orderByDesc(UserFollow::getCreateTime);


        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public List<UserFollow> listAll(QueryDTO queryDTO) {
        return lambdaQuery()
                .like(StringUtils.hasText(queryDTO.getS1()), UserFollow::getFollowerId, queryDTO.getS1())
                .last(queryDTO.getLimit() != null, "limit 0 ," + queryDTO.getLimit())
                .orderByDesc(UserFollow::getCreateTime).list();

    }

    @Override
    public UserFollow getUserFollowById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean deleteUserFollowById(Integer id) {
        return removeById(id);
    }

    @Override
    public boolean deleteUserFollowByIds(List<Integer> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public boolean addUserFollow(UserFollow obj) {
        return save(obj);
    }

    @Override
    public boolean updateUserFollowById(UserFollow obj) {
        return updateById(obj);
    }


    @Override
    public boolean follow(UserFollow obj) {
        Integer followerId = SecurityUtil.getCurrentUser().getId();
        UserFollow one = lambdaQuery()
                .eq(UserFollow::getFollowerId,followerId)
                .eq(UserFollow::getFolloweeId, obj.getFolloweeId())
                .one();
        if (one != null) {
            return deleteUserFollowById(one.getId());
        } else {
            obj.setFollowerId(followerId);
            return save(obj);
        }
    }

    @Override
    public boolean followed(UserFollow obj) {
        return lambdaQuery()
                .eq(UserFollow::getFollowerId, SecurityUtil.getCurrentUser().getId())
                .eq(UserFollow::getFolloweeId, obj.getFolloweeId())
                .exists();

    }

    @Override
    public List<SysUser> fansList() {
        List<SysUser> fansList=new ArrayList<>();
        List<UserFollow> list = lambdaQuery()
                .eq(UserFollow::getFolloweeId, SecurityUtil.getCurrentUser().getId())
                .list();
        for (UserFollow userFollow : list) {
            fansList.add(SecurityUtil.getUserById(userFollow.getFollowerId()));
        }
        return fansList;
    }

    @Override
    public List<SysUser> floowList() {
        List<SysUser> floowList=new ArrayList<>();
        List<UserFollow> list = lambdaQuery()
                .eq(UserFollow::getFollowerId, SecurityUtil.getCurrentUser().getId())
                .list();
        for (UserFollow userFollow : list) {
            floowList.add(SecurityUtil.getUserById(userFollow.getFolloweeId()));
        }
        return floowList;
    }
}
