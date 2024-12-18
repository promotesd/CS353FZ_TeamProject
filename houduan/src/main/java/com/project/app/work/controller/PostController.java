package com.project.app.work.controller;

import com.project.app.model.dto.PageDTO;
import com.project.app.model.dto.QueryDTO;
import com.project.app.util.R;
import com.project.app.work.entity.Post;
import com.project.app.work.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 帖子表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService iPostService;


    @PostMapping("page")
    @Operation(summary = "帖子表分页")
    public R page(@RequestBody PageDTO pageDTO) {
        return R.success(iPostService.page(pageDTO));
    }

    @PostMapping("pageSended")
    @Operation(summary = "帖子表分页")
    public R pageSended(@RequestBody PageDTO pageDTO) {
        return R.success(iPostService.pageSended(pageDTO));
    }

    @PostMapping("pageFront")
    @Operation(summary = "帖子表分页")
    public R pageFront(@RequestBody PageDTO pageDTO) {
        return R.success(iPostService.pageFront(pageDTO));
    }


    @PostMapping("listAll")
    @Operation(summary = "帖子表列表")
    public R listAll(@RequestBody QueryDTO queryDTO) {
        return R.success(iPostService.listAll(queryDTO));
    }

    @PostMapping("listAllFront")
    @Operation(summary = "帖子表列表")
    public R listAllFront(@RequestBody QueryDTO queryDTO) {
        return R.success(iPostService.listAllFront(queryDTO));
    }


    @GetMapping("postTag")
    @Operation(summary = "帖子标签")
    public R postTag() {
        return R.success(iPostService.postTag());
    }

    @GetMapping("getById/{id}")
    @Operation(summary = "帖子表详情")
    public R getPostById(@PathVariable("id") Integer id) {
        return R.success(iPostService.getPostById(id));
    }

    @DeleteMapping("deleteById/{id}")
    @Operation(summary = "帖子表删除")
    public R deletePostById(@PathVariable("id") Integer id) {
        return R.success(iPostService.deletePostById(id));
    }

    @DeleteMapping("deleteByIds")
    @Operation(summary = "帖子表批量删除")
    public R deletePostById(@RequestBody List<Integer> ids) {
        return R.success(iPostService.deletePostByIds(ids));
    }

    @PostMapping("add")
    @Operation(summary = "帖子表新增")
    public R addPost(@RequestBody Post obj) {
        return R.success(iPostService.addPost(obj));
    }


    @PutMapping("updateById")
    @Operation(summary = "帖子表更新")
    public R updatePostById(@RequestBody Post obj) {
        return R.success(iPostService.updatePostById(obj));
    }

}
