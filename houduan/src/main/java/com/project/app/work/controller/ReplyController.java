package com.project.app.work.controller;

import com.project.app.util.R;
import com.project.app.work.entity.Reply;
import com.project.app.work.service.ReplyService;
import com.project.app.model.dto.PageDTO;
import com.project.app.model.dto.QueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 回复表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
    public class ReplyController {

    private final ReplyService iReplyService;


    @PostMapping("page")
    @Operation(summary = "回复表分页")
    public R page(@RequestBody PageDTO pageDTO) {
        return R.success(iReplyService.page(pageDTO));
    }

    @PostMapping("pageSended")
    @Operation(summary = "回复表分页")
    public R pageSended(@RequestBody PageDTO pageDTO) {
        return R.success(iReplyService.pageSended(pageDTO));
    }

    @PostMapping("listAll")
    @Operation(summary = "回复表列表")
    public R listAll(@RequestBody QueryDTO queryDTO) {
        return R.success(iReplyService.listAll(queryDTO));
    }

    @GetMapping("getById/{id}")
    @Operation(summary = "回复表详情")
    public R getReplyById(@PathVariable("id") Integer id) {
        return R.success(iReplyService.getReplyById(id));
    }

    @DeleteMapping("deleteById/{id}")
    @Operation(summary = "回复表删除")
    public R deleteReplyById(@PathVariable("id") Integer id) {
        return R.success(iReplyService.deleteReplyById(id));
    }

    @DeleteMapping("deleteByIds")
    @Operation(summary = "回复表批量删除")
    public R deleteReplyById(@RequestBody List<Integer> ids) {
        return R.success(iReplyService.deleteReplyByIds(ids));
    }

    @PostMapping("add")
    @Operation(summary = "回复表新增")
    public R addReply(@RequestBody Reply obj) {
        return R.success(iReplyService.addReply(obj));
    }


    @PutMapping("updateById")
    @Operation(summary = "回复表更新")
    public R updateReplyById(@RequestBody Reply obj) {
        return R.success(iReplyService.updateReplyById(obj));
    }

}
