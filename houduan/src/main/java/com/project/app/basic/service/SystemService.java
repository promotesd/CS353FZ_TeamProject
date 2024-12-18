package com.project.app.basic.service;

import com.project.app.basic.entity.SysUser;
import com.project.app.model.dto.RegisterDTO;

/**
 * @author admin
 * @since 2024
 */
public interface SystemService {

    boolean register(SysUser registerDTO);


}
