package org.snbo.aclService.service.impl;


import org.snbo.SpringScurity.entity.SecurityUser;
import org.snbo.aclService.bean.User;
import org.snbo.aclService.service.PermissionService;
import org.snbo.aclService.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 自定义userDetailsService - 认证用户详情
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /***
     * 根据账号获取用户信息
     * @param username:
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        User user = userService.selectByUsername(username);

        // 判断用户是否存在
        if (null == user) {
            //throw new UsernameNotFoundException("用户名不存在！");
        }
        // 返回UserDetails实现类
        // 讲师数据库中查询的用户转换为SpringSecurity需要的登录用户类型
        org.snbo.SpringScurity.entity.User curUser = new org.snbo.SpringScurity.entity.User();
        BeanUtils.copyProperties(user, curUser);

        // 调acl模块下的service获取用户的权限信息
        List<String> authorities = permissionService.selectPermissionValueByUserId(user.getId());

        // 根据SpringSecurity需要的登录用户和用户的权限信息创建SecurityUser对象
        SecurityUser securityUser = new SecurityUser(curUser);
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }

}
