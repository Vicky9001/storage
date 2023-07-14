package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


@TableName("purchase_record")//当实体类和表名不一致的时候，用@tablename来指定表名
public class PurchaseRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long creatorId;
    private String creatorName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private Integer state;
    @TableField(value = "`desc`")
    private String desc;
    private Long supplierId;
    private Double goodsPrice;
    private Integer logisticsType;
    private Double logisticsPrice;
    private Double totalPrice;
    private String remark;
    @TableField(exist = false)
    private List<TaskRecord> task;
    @TableField(exist = false)
    private List<GoodsBatch> goods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(Integer logisticsType) {
        this.logisticsType = logisticsType;
    }

    public Double getLogisticsPrice() {
        return logisticsPrice;
    }

    public void setLogisticsPrice(Double logisticsPrice) {
        this.logisticsPrice = logisticsPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<GoodsBatch> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBatch> goods) {
        this.goods = goods;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<TaskRecord> getTask() {
        return task;
    }

    public void setTask(List<TaskRecord> task) {
        this.task = task;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}