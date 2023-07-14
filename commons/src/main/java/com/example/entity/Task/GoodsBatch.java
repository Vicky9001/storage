package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Info.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


@TableName("goods_batch")//当实体类和表名不一致的时候，用@tablename来指定表名
public class GoodsBatch {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long relatedId;//关联批次
    private Long goodsId;//货物id
    private Integer recordType;
    private Long recordId;
    private Float num;
    private Long supplierId;
    private Long positionId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date manufactureDate;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date expirationDate;
    private Float unitPrice;
    private Float totalPrice;
    private Float unitWeight;
    private Float totalWeight;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date inDate;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date outDate;
    @TableField(exist = false)
    private Goods goods;
    @TableField(exist = false)
    private List<Position> positionList;
    @TableField(exist = false)
    private Float delnum;
    @TableField(exist = false)
    private Supplier supplier;
    @TableField(exist = false)
    private Long areaId;
    @TableField(exist = false)
    private String houseName;

    public GoodsBatch() {
    }

    public GoodsBatch(Long relatedId, Long goodsId, Integer recordType, Long recordId, Float num, Long supplierId, Date manufactureDate, Date expirationDate, Float unitPrice, Float totalPrice, Float unitWeight, Float totalWeight) {
        this.relatedId = relatedId;
        this.goodsId = goodsId;
        this.recordType = recordType;
        this.recordId = recordId;
        this.num = num;
        this.supplierId = supplierId;
        this.manufactureDate = manufactureDate;
        this.expirationDate = expirationDate;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.unitWeight = unitWeight;
        this.totalWeight = totalWeight;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Float unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Float getDelnum() {
        return delnum;
    }

    public void setDelnum(Float delnum) {
        this.delnum = delnum;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getPositionId() {
        return positionId;
    }
}