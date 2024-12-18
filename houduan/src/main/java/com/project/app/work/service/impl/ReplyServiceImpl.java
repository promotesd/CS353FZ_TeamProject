package com.project.app.work.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.app.basic.entity.SysUser;
import com.project.app.model.dto.PageDTO;
import com.project.app.model.dto.QueryDTO;
import com.project.app.util.SecurityUtil;
import com.project.app.work.entity.Post;
import com.project.app.work.entity.Reply;
import com.project.app.work.mapper.ReplyMapper;
import com.project.app.work.service.PostService;
import com.project.app.work.service.ReplyService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 回复表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Resource
    @Lazy
    private PostService postService;

    @Override
    public Page<Reply> page(PageDTO pageDTO) {

        SysUser currentUser = SecurityUtil.getCurrentUser();
        LambdaQueryChainWrapper<Reply> wrapper = lambdaQuery()
                .eq(!currentUser.getRole().equals("ADMIN"), Reply::getCreateBy, currentUser.getUsername())
                .like(StringUtils.hasText(pageDTO.getSearch()), Reply::getContent, pageDTO.getSearch())
                .orderByDesc(Reply::getCreateTime);


        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public Page<Reply> pageSended(PageDTO pageDTO) {
        SysUser currentUser = SecurityUtil.getCurrentUser();
        LambdaQueryChainWrapper<Reply> wrapper = lambdaQuery()
                .eq( Reply::getCreateBy, currentUser.getUsername())
                .like(StringUtils.hasText(pageDTO.getSearch()), Reply::getContent, pageDTO.getSearch())
                .orderByDesc(Reply::getCreateTime);


        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public List<Reply> listAll(QueryDTO queryDTO) {
        return lambdaQuery().like(StringUtils.hasText(queryDTO.getS1()), Reply::getPostId, queryDTO.getS1()).last(queryDTO.getLimit() != null, "limit 0 ," + queryDTO.getLimit()).orderByDesc(Reply::getCreateTime).list();

    }

    @Override
    public Reply getReplyById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean deleteReplyById(Integer id) {
        return removeById(id);
    }

    @Override
    public boolean deleteReplyByIds(List<Integer> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public boolean addReply(Reply obj) {
        Post post = postService.getById(obj.getPostId());
        post.setReplyCount(post.getReplyCount() + 1);
        postService.updateById(post);
        return save(obj);
    }

    @Override
    public boolean updateReplyById(Reply obj) {
        return updateById(obj);
    }
}
