package com.example.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Info.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    @Select("<script>"
            +"select * from department where 1=1"
            +"<if test='name!=\"\" and name!= null'>"
            +"and department_name like CONCAT(CONCAT('%',#{name}),'%')"
            +"</if>"
            +"</script>")
    List<Department> getDepartment(@Param("name") String name);

    @Select("select * from department where department_name = #{name}")
    Department selectByName(String name);
}
