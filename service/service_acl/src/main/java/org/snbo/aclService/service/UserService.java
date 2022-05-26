package org.snbo.aclService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.aclService.bean.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface UserService extends IService<User> {

    /**
     * 从数据库中取出用户信息
     *
     * @param username 用户名
     * @return 返回具体用户
     */
    User selectByUsername(String username);
}
