package com.project.app.basic.controller;

import com.project.app.basic.entity.SysUser;
import com.project.app.basic.service.SystemService;
import com.project.app.util.CaptchaGenerator;
import com.project.app.util.R;
import com.project.app.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;


    @PostMapping("register")
    @Operation(summary = "Registered Users")
    public R addSysUser(@RequestBody SysUser obj) {
        return R.success(systemService.register(obj), "Registration is successful");
    }


    @Operation(summary = "captcha")
    @GetMapping(value = "/captcha")
    public R captcha() throws IOException {
        return R.success(CaptchaGenerator.generate());
    }


    @Operation(summary = "verification")
    @GetMapping(value = "/verification")
    public R verification(String uuid,String userCode) {
        return R.success(CaptchaGenerator.verification(uuid, userCode));
    }


    @Operation(summary = "curryUser")
    @GetMapping(value = "/curryUser")
    public R curryUser() {
        return R.success(SecurityUtil.getCurrentUser());
    }


}
