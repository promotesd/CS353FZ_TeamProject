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
import com.project.app.work.mapper.PostMapper;
import com.project.app.work.service.PostService;
import com.project.app.work.service.ReplyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 帖子表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2024
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private ReplyService replyService;

    @Override
    public Page<Post> page(PageDTO pageDTO) {
        SysUser currentUser = SecurityUtil.getCurrentUser();
        LambdaQueryChainWrapper<Post> wrapper = lambdaQuery()
                .eq(!currentUser.getRole().equals("ADMIN"), Post::getCreateBy, currentUser.getUsername())
                .like(StringUtils.hasText(pageDTO.getSearch()), Post::getTitle, pageDTO.getSearch())
                .orderByDesc(Post::getCreateTime);
        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public Page<Post> pageSended(PageDTO pageDTO) {
        SysUser currentUser = SecurityUtil.getCurrentUser();
        LambdaQueryChainWrapper<Post> wrapper = lambdaQuery()
                .eq(Post::getCreateBy, currentUser.getUsername())
                .like(StringUtils.hasText(pageDTO.getSearch()), Post::getTitle, pageDTO.getSearch())
                .orderByDesc(Post::getCreateTime);
        return wrapper.page(pageDTO.getMybatisPage());
    }

    @Override
    public Page<Post> pageFront(PageDTO pageDTO) {
        LambdaQueryChainWrapper<Post> wrapper = lambdaQuery().like(StringUtils.hasText(pageDTO.getSearch()), Post::getTitle, pageDTO.getSearch()).eq(StringUtils.hasText(pageDTO.getCategory()), Post::getCategory, pageDTO.getCategory()).orderByDesc(Post::getCreateTime, Post::getIsHot);
        Page<Post> page = wrapper.page(pageDTO.getMybatisPage());

        for (Post record : page.getRecords()) {
            record.setUser(SecurityUtil.getUserByName(record.getCreateBy()));
        }
        return page;
    }

    @Override
    public List<Post> listAll(QueryDTO queryDTO) {
        return lambdaQuery().like(StringUtils.hasText(queryDTO.getS1()), Post::getTitle, queryDTO.getS1()).last(queryDTO.getLimit() != null, "limit 0 ," + queryDTO.getLimit()).orderByDesc(Post::getCreateTime).list();

    }

    @Override
    public List<Post> listAllFront(QueryDTO queryDTO) {
        return lambdaQuery().like(StringUtils.hasText(queryDTO.getS1()), Post::getTitle, queryDTO.getS1()).last(queryDTO.getLimit() != null, "limit 0 ," + queryDTO.getLimit()).orderByDesc(Post::getCreateTime).list();

    }

    @Override
    public List<String> postTag() {

        return lambdaQuery().select(Post::getCategory).list().stream().map(Post::getCategory).distinct().toList();

    }

    @Override
    public Post getPostById(Integer id) {
        List<Reply> replies = replyService.lambdaQuery().eq(Reply::getPostId, id).list();
        for (Reply reply : replies) {
            reply.setSysUser(SecurityUtil.getUserByName(reply.getCreateBy()));
        }
        Post post = getById(id);
        SysUser user = SecurityUtil.getUserByName(post.getCreateBy());
        post.setReplies(replies);
        post.setUser(user);
        return post;
    }

    @Override
    public boolean deletePostById(Integer id) {
        return removeById(id);
    }

    @Override
    public boolean deletePostByIds(List<Integer> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public boolean addPost(Post obj) {
        return save(obj);
    }

    @Override
    public boolean updatePostById(Post obj) {
        return updateById(obj);
    }
}
