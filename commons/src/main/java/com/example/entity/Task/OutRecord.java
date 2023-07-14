package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


@TableName("out_record")//当实体类和表名不一致的时候，用@tablename来指定表名
public class OutRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long creatorId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private Integer state;
    @TableField(value = "`desc`")
    private String desc;
    private Long departmentId;
    private Double goodsPrice;
    private Integer logisticsType;
    private Double logisticsPrice;
    private Double totalPrice;
    private String remark;
    private Float num;
    private Long goodsId;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}