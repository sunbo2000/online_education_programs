package org.snbo.aclService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.aclService.bean.Role;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户获取角色数据
     *
     * @param userId 用户id
     * @return Map集合
     */
    Map<String, Object> findRoleByUserId(String userId);

    /**
     * 根据用户分配角色
     *
     * @param roleId 角色id
     * @param userId 用户id
     */
    void saveUserRoleRelationShip(String userId, String[] roleId);

    /**
     * 根据用户id获取角色
     *
     * @param id 用户id
     * @return Role类型的集合
     */
    List<Role> selectRoleByUserId(String id);
}
