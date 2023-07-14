package com.example.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User.UserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    //批量插入用户角色关联关系
    @Insert({
            "<script>",
            "INSERT INTO user_role (user_id, role_id) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId},#{item.roleId})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("list") List<UserRole> userRoles);
}
