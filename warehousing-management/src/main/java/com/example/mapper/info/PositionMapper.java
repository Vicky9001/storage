package com.example.mapper.info;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Info.Position;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PositionMapper extends BaseMapper<Position> {

    @Select("<script>"
            +"select max(shelve_id) from position where area_id=#{id}"
            +"</script>")
    Long getShelveId(@Param("id") Long id);


    @Select("<script>"
            +"select * from position where 1=1"
            +"<if test='area!= null'>"
            +"and area_id=#{area}"
            +"</if>"
            +"<if test='shelve!= null'>"
            +"and shelve_id=#{shelve}"
            +"</if>"
            +"</script>")
    Position selectPosition(@Param("area") Long area,@Param("shelve") Long shelve);

    @Select("<script>"
            +"select sum(goods_num) from position "
            +"where area_id=#{id}"
            +"</script>")
    Integer selectGoodsNum(@Param("id") Long id);

    @Delete("<script>"
            +"delete from position where area_id=#{id}"
            +"</script>")
    int deleteByArea(@Param("id") Long id);
}
