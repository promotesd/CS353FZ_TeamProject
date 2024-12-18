package com.project.app.work.service;

import com.project.app.work.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * <p>
 * 举报表 服务类
 * </p>
 *
 * @author admin
 * @since 2024
 */
public interface ReportService extends IService<Report> {
        Page<Report> page(PageDTO pageDTO);
        List<Report> listAll(QueryDTO queryDTO);
        Report getReportById(Integer id);
        boolean deleteReportById(Integer id);
        boolean deleteReportByIds(List<Integer> ids);
        boolean addReport(Report obj);
        boolean updateReportById(Report obj);
}
