package com.project.app.work.service;

import com.project.app.work.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * <p>
 * 帖子表 服务类
 * </p>
 *
 * @author admin
 * @since 2024
 */
public interface PostService extends IService<Post> {
        Page<Post> page(PageDTO pageDTO);
        Page<Post> pageSended(PageDTO pageDTO);
        Page<Post> pageFront(PageDTO pageDTO);
        List<Post> listAll(QueryDTO queryDTO);
        List<Post> listAllFront(QueryDTO queryDTO);

        List<String> postTag();
        Post getPostById(Integer id);
        boolean deletePostById(Integer id);
        boolean deletePostByIds(List<Integer> ids);
        boolean addPost(Post obj);
        boolean updatePostById(Post obj);
}
