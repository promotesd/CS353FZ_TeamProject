package com.project.app.basic.controller;

import com.project.app.basic.entity.SysUser;
import com.project.app.basic.service.SysUserService;
import com.project.app.model.dto.PageDTO;
import com.project.app.util.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/sysUser")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService iSysUserService;


    @PostMapping("page")
    @Operation(summary = "page")
    public R page(@RequestBody PageDTO pageDTO) {
        return R.success(iSysUserService.page(pageDTO));
    }

    @GetMapping("listAll")
    @Operation(summary = "listAll")
    public R listAll() {
        return R.success(iSysUserService.listAll());
    }




    @GetMapping("getById/{id}")
    @Operation(summary = "getById")
    public R getSysUserById(@PathVariable("id") Long id) {
        return R.success(iSysUserService.getSysUserById(id));
    }

    @DeleteMapping("deleteById/{id}")
    @Operation(summary = "deleteById")
    public R deleteSysUserById(@PathVariable("id") Long id) {
        return R.success(iSysUserService.deleteSysUserById(id));
    }

    @DeleteMapping("deleteByIds")
    @Operation(summary = "deleteByIds")
    public R deleteSysUserById(@RequestBody List<Long> ids) {
        return R.success(iSysUserService.deleteSysUserByIds(ids));
    }


    @PostMapping("add")
    @Operation(summary = "add")
    public R addSysUser(@RequestBody SysUser obj) {
        return R.success(iSysUserService.addSysUser(obj));
    }


    @PutMapping("updateById")
    @Operation(summary = "updateById")
    public R updateSysUserById(@RequestBody SysUser obj) {
        return R.success(iSysUserService.updateSysUserById(obj));
    }


    @PutMapping("updateSysUserByName")
    @Operation(summary = "updateSysUserByName")
    public R updateSysUserByName(@RequestBody SysUser obj) {
        return R.success(iSysUserService.updateSysUserByName(obj));
    }


}
