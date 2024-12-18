package com.project.app.work.controller;

import com.project.app.util.R;
import com.project.app.work.entity.Report;
import com.project.app.work.service.ReportService;
import com.project.app.model.dto.PageDTO;
import com.project.app.model.dto.QueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 举报表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2024
 */
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
    public class ReportController {

    private final ReportService iReportService;


    @PostMapping("page")
    @Operation(summary = "举报表分页")
    public R page(@RequestBody PageDTO pageDTO) {
        return R.success(iReportService.page(pageDTO));
    }

    @PostMapping("listAll")
    @Operation(summary = "举报表列表")
    public R listAll(@RequestBody QueryDTO queryDTO) {
        return R.success(iReportService.listAll(queryDTO));
    }

    @GetMapping("getById/{id}")
    @Operation(summary = "举报表详情")
    public R getReportById(@PathVariable("id") Integer id) {
        return R.success(iReportService.getReportById(id));
    }

    @DeleteMapping("deleteById/{id}")
    @Operation(summary = "举报表删除")
    public R deleteReportById(@PathVariable("id") Integer id) {
        return R.success(iReportService.deleteReportById(id));
    }

    @DeleteMapping("deleteByIds")
    @Operation(summary = "举报表批量删除")
    public R deleteReportById(@RequestBody List<Integer> ids) {
        return R.success(iReportService.deleteReportByIds(ids));
    }

    @PostMapping("add")
    @Operation(summary = "举报表新增")
    public R addReport(@RequestBody Report obj) {
        return R.success(iReportService.addReport(obj));
    }


    @PutMapping("updateById")
    @Operation(summary = "举报表更新")
    public R updateReportById(@RequestBody Report obj) {
        return R.success(iReportService.updateReportById(obj));
    }

}
