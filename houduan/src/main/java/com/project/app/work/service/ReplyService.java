package com.project.app.work.service;

import com.project.app.work.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.app.model.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * <p>
 * 回复表 服务类
 * </p>
 *
 * @author admin
 * @since 2024
 */
public interface ReplyService extends IService<Reply> {
        Page<Reply> page(PageDTO pageDTO);
        Page<Reply> pageSended(PageDTO pageDTO);
        List<Reply> listAll(QueryDTO queryDTO);
        Reply getReplyById(Integer id);
        boolean deleteReplyById(Integer id);
        boolean deleteReplyByIds(List<Integer> ids);
        boolean addReply(Reply obj);
        boolean updateReplyById(Reply obj);
}
