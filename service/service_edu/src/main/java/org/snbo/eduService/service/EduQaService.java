package org.snbo.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.eduService.bean.EduQa;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-28
 */
public interface EduQaService extends IService<EduQa> {

    /**
     * 获取话题列表
     * @return
     */
    Map<String,Object> getTopic(String length, String context);

    Map<String, Object> getCommentPage(String partId, Page<EduQa> page);

    Map<String, Object> getTopicPage(String partId, Long current, Long size);

}
