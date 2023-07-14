package com.example.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Info.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from goods where goods_name = #{name}")
    @Results({
            @Result(column = "goods_id",property = "goodsId"),
            @Result(column = "goods_type",property = "goodsType"),
            @Result(column = "goods_name",property = "goodsName"),
            @Result(column = "goods_info",property = "goodsInfo"),
            @Result(column = "unit",property = "unit"),
            @Result(column = "grounding_num",property = "groundingNum")
    })
    Goods selectByName(String name);

}
