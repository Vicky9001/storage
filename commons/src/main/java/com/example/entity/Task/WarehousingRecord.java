package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Info.Position;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

@TableName("warehousing_record")
public class WarehousingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private Integer state;
    @TableField(value = "`desc`")
    private String desc;
    private Double goodsPrice;
    private Long creatorId;
    private String creatorName;
    @TableField(exist = false)
    private List<GoodsBatch> goodsBatches;
    @TableField(exist = false)
    private List<TaskRecord> taskRecords;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<GoodsBatch> getGoodsBatches() {
        return goodsBatches;
    }

    public void setGoodsBatches(List<GoodsBatch> goodsBatches) {
        this.goodsBatches = goodsBatches;
    }

    public List<TaskRecord> getTaskRecords() {
        return taskRecords;
    }

    public void setTaskRecords(List<TaskRecord> taskRecords) {
        this.taskRecords = taskRecords;
    }
}
