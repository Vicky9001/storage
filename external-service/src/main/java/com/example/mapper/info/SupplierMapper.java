package com.example.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Info.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    @Select("<script>"
            +"select * from supplier where 1=1"
            +"<if test='name!=\"\" and name!= null'>"
            +"and supplier_name like CONCAT(CONCAT('%',#{name}),'%')"
            +"</if>"
            +"</script>")
    @Results({
            @Result(column = "supplier_id",property = "supplierId"),
            @Result(column = "supplier_name",property = "supplierName"),
            @Result(column = "address",property = "address"),
            @Result(column = "phone",property = "phone")
    })
    List<Supplier> getSupplier(@Param("name") String name);

    @Select("select * from Supplier where supplier_name = #{name}")
    @Results({
            @Result(column = "supplier_id",property = "supplierId"),
            @Result(column = "supplier_name",property = "supplierName"),
            @Result(column = "address",property = "address"),
            @Result(column = "phone",property = "phone")
    })
    Supplier selectByName(String name);
}
