package com.example.entity.Info;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.WarningRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

public class Position {
    @TableId(type = IdType.AUTO)
    private Long positionId;
    private Long areaId;
    private Long shelveId;
    @TableField(updateStrategy = FieldStrategy.IGNORED,jdbcType = JdbcType.BIGINT)
    private Long goodsType;
    private Float goodsNum;
    private Integer state;
    @TableField(updateStrategy = FieldStrategy.IGNORED,jdbcType = JdbcType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date manufactureDate;
    @TableField(updateStrategy = FieldStrategy.IGNORED,jdbcType = JdbcType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date expirationDate;
    @TableField(updateStrategy = FieldStrategy.IGNORED,jdbcType = JdbcType.BIGINT)
    private Long batchInfo;
    private Float capacity;
    @TableField(updateStrategy = FieldStrategy.IGNORED,jdbcType = JdbcType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date inDate;
    @TableField(exist = false)
    private Area area;
    @TableField(exist = false)
    private Goods goods;
    @TableField(exist = false)
    private List<WarningRecord> warningRecords;

    public Position() {
        this.goodsType = null;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getShelveId() {
        return shelveId;
    }

    public void setShelveId(Long shelveId) {
        this.shelveId = shelveId;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }

    public Float getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Float goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Long getBatchInfo() {
        return batchInfo;
    }

    public void setBatchInfo(Long batchInfo) {
        this.batchInfo = batchInfo;
    }

    public Float getCapacity() {
        return capacity;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public List<WarningRecord> getWarningRecords() {
        return warningRecords;
    }

    public void setWarningRecords(List<WarningRecord> warningRecords) {
        this.warningRecords = warningRecords;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionId=" + positionId +
                ", areaId=" + areaId +
                ", shelveId=" + shelveId +
                ", goodsType=" + goodsType +
                ", goodsNum=" + goodsNum +
                ", state=" + state +
                ", manufactureDate=" + manufactureDate +
                ", expirationDate=" + expirationDate +
                ", batchInfo=" + batchInfo +
                ", capacity=" + capacity +
                ", inDate=" + inDate +
                ", area=" + area +
                ", goods=" + goods +
                ", warningRecords=" + warningRecords +
                '}';
    }
}
