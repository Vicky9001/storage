package com.example.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Info.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {
    @Select("<script>"
            +"select * from area where 1=1"
            +"<if test='name!=\"\" and name!= null'>"
            +"and house_name like CONCAT(CONCAT('%',#{name}),'%')"
            +"</if>"
            +"</script>")
    List<Area> getArea(@Param("name") String name);

    @Select("<script>"
            +"select distinct house_name from area"
            +"</script>")
    List<String> getHouse();

    @Select("<script>"
            +"select * from area where 1=1"
            +"<if test='house!= null'>"
            +"and house_name=#{house}"
            +"</if>"
            +"<if test='type!= null'>"
            +"and area_type=#{type}"
            +"</if>"
            +"</script>")
    Area selectArea(@Param("house") String house, @Param("type") Integer type);

    @Select("select * from area where id = #{id}")
    Area get(Long id);
}
