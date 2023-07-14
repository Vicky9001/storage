package com.example.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

//用于操作用户表,MyBaits会根据Mapper注解，动态实现UserMapper接口（实现类），动态代理技术
//Spring会自动创建UserMapper接口实现类对应的实例
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("<script>"
            +"select * from sys_role where 1=1"
            +"<if test='name!=\"\" and name!= null'>"
            +"and role_name like CONCAT(CONCAT('%',#{name}),'%')"
            +"</if>"
            +"</script>")
    @Results({
            @Result(column = "role_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_desc",property = "roleDesc")
    })
    List<Role> select(@Param("name") String name);

    @Select("select * from sys_role where role_id=#{id}")
    @Results({
            @Result(column = "role_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_desc",property = "roleDesc")
    })
    Role selectByRoleId(@Param("id") Long id);

    @Select("select * from sys_role;")
    @Results({
            @Result(column = "role_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_desc",property = "roleDesc")
    })
    List<Role> selectAll();

    @Select("select * from sys_role where role_name=#{roleName}")
    @Results({
            @Result(column = "role_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_desc",property = "roleDesc")
    })
    Role selectByRoleName(@Param("roleName") String roleName);

    @Insert("insert into sys_role(role_name,role_desc) values(#{roleName},#{roleDesc})")
    int add(Role role);

    @Update("update sys_role set role_name=#{roleName}, role_desc=#{roleDesc} where role_id=#{id}")
    int updateRole(Role role);

    @Delete("delete from sys_role where role_id=#{id}")
    int deleteByRoleId(@Param("id") Long id);

    @Delete("<script>" +
            "delete from sys_role where role_id in " +
            "<foreach collection='array' open='(' item='id' separator=',' close=')'> #{id}" +
            "</foreach>" +
            "</script>")
    int deleteByIds(List<Long> ids);

//    @Select("select * from sys_role left join user_role on sys_role.role_id=user_role.role_id  where user_role.user_id=#{userId}")
//    List<Role> getListByUserId(Long userId);

    @Select("SELECT r.* FROM sys_role r INNER JOIN user_role ur ON r.role_id = ur.role_id WHERE ur.user_id = #{userId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name")
    })
    List<Role> findByUserId(Long userId);
}
