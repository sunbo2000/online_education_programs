package org.snbo.aclService.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.aclService.bean.UserRole;
import org.snbo.aclService.mapper.UserRoleMapper;
import org.snbo.aclService.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
