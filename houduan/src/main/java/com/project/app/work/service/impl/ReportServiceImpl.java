package com.project.app.work.service.impl;

import com.project.app.basic.entity.SysUser;
import com.project.app.util.SecurityUtil;
import com.project.app.work.entity.Post;
import com.project.app.work.entity.Report;
import com.project.app.work.mapper.ReportMapper;
import com.project.app.work.service.ReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.stereotype.Service;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * <p>
 * 举报表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Override
    public Page<Report> page(PageDTO pageDTO) {


        SysUser currentUser = SecurityUtil.getCurrentUser();

        LambdaQueryChainWrapper<Report> wrapper = lambdaQuery()
                .eq(!currentUser.getRole().equals("ADMIN"), Report::getCreateBy, currentUser.getUsername())
                .like(StringUtils.hasText(pageDTO.getSearch()), Report::getPostId, pageDTO.getSearch())
                .orderByDesc(Report::getCreateTime);


        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public List<Report> listAll(QueryDTO queryDTO) {
        return lambdaQuery()
                .like(StringUtils.hasText(queryDTO.getS1()), Report::getPostId, queryDTO.getS1())
                .last(queryDTO.getLimit() != null, "limit 0 ," + queryDTO.getLimit())
                .orderByDesc(Report::getCreateTime).list();

    }

    @Override
    public Report getReportById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean deleteReportById(Integer id) {
        return removeById(id);
    }

    @Override
    public boolean deleteReportByIds(List<Integer> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public boolean addReport(Report obj) {
        return save(obj);
    }

    @Override
    public boolean updateReportById(Report obj) {
        return updateById(obj);
    }
}
