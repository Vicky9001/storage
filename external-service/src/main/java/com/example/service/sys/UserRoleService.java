package com.example.service.sys;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.User.UserRole;
import com.example.mapper.sys.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)

public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    public void updateUserRoles(Integer userId, List<Integer> roleIds) {
        // 先删除原有的用户角色关系
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        // 添加新的用户角色关系
        List<UserRole> userRoles = roleIds.stream().map(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        userRoleMapper.insertBatch(userRoles);
    }
}
