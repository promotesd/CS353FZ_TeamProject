package com.project.app.basic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.basic.entity.SysUser;
import com.project.app.model.dto.PageDTO;

import java.util.List;

/**
 * @author admin
 * @since 2024
 */
public interface SysUserService extends IService<SysUser> {
    Page<SysUser> page(PageDTO pageDTO);

    List<SysUser> listAll();



    SysUser getSysUserById(Long id);

    boolean deleteSysUserById(Long id);

    boolean deleteSysUserByIds(List<Long> ids);

    boolean addSysUser(SysUser obj);

    boolean updateSysUserById(SysUser obj);

    boolean updateSysUserByName(SysUser obj);
}
