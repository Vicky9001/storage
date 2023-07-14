package com.example.entity.Info;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class Goods {
    @TableId(type = IdType.AUTO)
    private Long goodsId;
    private Integer goodsType;
    private String goodsName;
    private String goodsInfo;
    private String unit;
    private Float groundingNum;
    @TableField(exist = false)
    private Float unitPrice;
    @TableField(exist = false)
    private Float unitWeight;
    @TableField(exist = false)
    private Float price;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getGroundingNum() {
        return groundingNum;
    }

    public void setGroundingNum(Float groundingNum) {
        this.groundingNum = groundingNum;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Float unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
