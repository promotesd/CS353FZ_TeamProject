package com.project.app.basic.service.impl;

import com.alibaba.fastjson.JSON;
import com.project.app.basic.entity.SysUser;
import com.project.app.basic.service.SysUserService;
import com.project.app.basic.service.SystemService;
import com.project.app.execptions.MyRuntimeException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SystemServiceImpl implements SystemService {

    @Resource
    private SysUserService sysUserService;


    @Override
    public boolean register(SysUser registerDTO) {
        if (!StringUtils.hasText(registerDTO.getUsername())
                || !StringUtils.hasText(registerDTO.getPassword())
                || !StringUtils.hasText(registerDTO.getAvatar())) {
            throw new MyRuntimeException("请填写完整信息");
        }


        boolean exists = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, registerDTO.getUsername())
                .exists();

        if (exists) {
            throw new MyRuntimeException("用户已存在");
        }


        SysUser newUser = JSON.parseObject(JSON.toJSONString(registerDTO), SysUser.class);
        return sysUserService.save(newUser);
    }

}
