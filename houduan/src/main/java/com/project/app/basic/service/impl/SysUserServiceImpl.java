package com.project.app.basic.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.app.basic.entity.SysUser;
import com.project.app.basic.mapper.SysUserMapper;
import com.project.app.basic.service.SysUserService;
import com.project.app.model.dto.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public Page<SysUser> page(PageDTO pageDTO) {

        return lambdaQuery()
                .eq(SysUser::getRole, pageDTO.getRole())
                .like(StringUtils.hasText(pageDTO.getSearch()), SysUser::getUsername, pageDTO.getSearch())
                .orderByDesc(SysUser::getCreateTime).page(pageDTO.getMybatisPage());
    }

    @Override
    public List<SysUser> listAll() {
        List<SysUser> list = list();
        for (SysUser sysUser : list) {
            sysUser.setPassword(null);
        }
        return list;
    }


    @Override
    public SysUser getSysUserById(Long id) {
        return getById(id);
    }

    @Override
    public boolean deleteSysUserById(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteSysUserByIds(List<Long> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public boolean addSysUser(SysUser obj) {
        return save(obj);
    }

    @Override
    public boolean updateSysUserById(SysUser obj) {
        return updateById(obj);
    }

    @Override
    public boolean updateSysUserByName(SysUser obj) {
        return lambdaUpdate()
                .eq(SysUser::getUsername, obj.getUsername())
                .set(SysUser::getPassword, obj.getPassword())
                .update();
    }


}
