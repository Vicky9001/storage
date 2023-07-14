package com.example.entity.Statistics;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Info.Department;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Logistics;
import com.example.entity.Info.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

@TableName("in_and_out")
public class InAndOut {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date time;
    private Integer type;
    private Float goodsNum;
    private Double goodsPrice;
    private Double logisticsPrice;
    private Double StorageCost;
    private Double totalPrice;
    @TableField(exist = false)
    private List<Goods> goods;
    @TableField(exist = false)
    private List<Logistics> logistics;
    @TableField(exist = false)
    private List<Supplier> suppliers;
    @TableField(exist = false)
    private List<Department> departments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Float getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Float goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getLogisticsPrice() {
        return logisticsPrice;
    }

    public void setLogisticsPrice(Double logisticsPrice) {
        this.logisticsPrice = logisticsPrice;
    }

    public Double getStorageCost() {
        return StorageCost;
    }

    public void setStorageCost(Double storageCost) {
        StorageCost = storageCost;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public List<Logistics> getLogistics() {
        return logistics;
    }

    public void setLogistics(List<Logistics> logistics) {
        this.logistics = logistics;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
