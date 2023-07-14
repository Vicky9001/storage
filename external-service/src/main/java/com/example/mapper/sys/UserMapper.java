package com.example.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

//用于操作用户表,MyBaits会根据Mapper注解，动态实现UserMapper接口（实现类），动态代理技术
//Spring会自动创建UserMapper接口实现类对应的实例
@Mapper
public interface UserMapper extends BaseMapper<User> {

    //根据token查询信息   select * from user where username =
    @Select("select * from sys_user where user_name = #{username}")
    User selectByName(String username);

    @Select("SELECT * FROM sys_user WHERE user_name = #{username} AND password = #{password}")
    User getInfo(@Param("username") String username, @Param("password") String password);

    @Select("SELECT DISTINCT * " +
            "FROM sys_user u LEFT JOIN user_role ur ON u.user_id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.role_id " +
            "WHERE (u.user_name LIKE CONCAT('%',#{userName},'%') OR #{userName} IS NULL OR #{userName} = '') " +
            "AND (u.real_name LIKE CONCAT('%',#{realName},'%') OR #{realName} IS NULL OR #{realName} = '') " +
            "AND (r.role_id = #{roleId} OR #{roleId} IS NULL) " +
            "ORDER BY u.user_id ASC")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "realName", column = "real_name"),
            @Result(property = "roles", column = "user_id", many = @Many(select = "com.example.mapper.sys.RoleMapper.findByUserId"))
    })
    List<User> getUserList(@Param("userName") String userName, @Param("realName") String realName, @Param("roleId") Long roleId);

    @Delete("delete from user_role where user_id=#{id}")
    int deleteRoleByUserId(@Param("id") Long id);
}
