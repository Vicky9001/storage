package com.example.entity.Info;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class GoodsSupplier {

    private Long goodsId;
    private Long supplierId;
    private Float unitPrice;
    private Float unitWeight;


    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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
}
