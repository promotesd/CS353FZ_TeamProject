package com.project.app.work.controller;

import com.project.app.model.dto.PageDTO;
import com.project.app.model.dto.QueryDTO;
import com.project.app.util.R;
import com.project.app.work.entity.UserFollow;
import com.project.app.work.service.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户关注表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/userFollow")
@RequiredArgsConstructor
public class UserFollowController {

    private final UserFollowService iUserFollowService;


    @PostMapping("page")
    @Operation(summary = "用户关注表分页")
    public R page(@RequestBody PageDTO pageDTO) {
        return R.success(iUserFollowService.page(pageDTO));
    }






    @PostMapping("listAll")
    @Operation(summary = "用户关注表列表")
    public R listAll(@RequestBody QueryDTO queryDTO) {
        return R.success(iUserFollowService.listAll(queryDTO));
    }

    @GetMapping("getById/{id}")
    @Operation(summary = "用户关注表详情")
    public R getUserFollowById(@PathVariable("id") Integer id) {
        return R.success(iUserFollowService.getUserFollowById(id));
    }

    @DeleteMapping("deleteById/{id}")
    @Operation(summary = "用户关注表删除")
    public R deleteUserFollowById(@PathVariable("id") Integer id) {
        return R.success(iUserFollowService.deleteUserFollowById(id));
    }

    @DeleteMapping("deleteByIds")
    @Operation(summary = "用户关注表批量删除")
    public R deleteUserFollowById(@RequestBody List<Integer> ids) {
        return R.success(iUserFollowService.deleteUserFollowByIds(ids));
    }

    @PostMapping("add")
    @Operation(summary = "用户关注表新增")
    public R addUserFollow(@RequestBody UserFollow obj) {
        return R.success(iUserFollowService.addUserFollow(obj));
    }



    @PostMapping("follow")
    @Operation(summary = "用户关注")
    public R follow(@RequestBody UserFollow obj) {
        return R.success(iUserFollowService.follow(obj));
    }


    @PostMapping("followed")
    @Operation(summary = "是否关注")
    public R followed(@RequestBody UserFollow obj) {
        return R.success(iUserFollowService.followed(obj));
    }



    @GetMapping("floowList")
    @Operation(summary = "关注列表")
    public R floowList() {
        return R.success(iUserFollowService.floowList());
    }

    @GetMapping("fansList")
    @Operation(summary = "粉丝列表")
    public R fansList() {
        return R.success(iUserFollowService.fansList());
    }


    @PutMapping("updateById")
    @Operation(summary = "用户关注表更新")
    public R updateUserFollowById(@RequestBody UserFollow obj) {
        return R.success(iUserFollowService.updateUserFollowById(obj));
    }

}
