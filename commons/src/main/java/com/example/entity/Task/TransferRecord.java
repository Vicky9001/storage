package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Info.Position;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


@TableName("transfer_record")//当实体类和表名不一致的时候，用@tablename来指定表名
public class TransferRecord {
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
    private Long originPosition;
    private Long targetPosition;
    @TableField(exist = false)
    private Position originP;
    @TableField(exist = false)
    private Position targetP;
    private Float num;
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

    public Long getOriginPosition() {
        return originPosition;
    }

    public void setOriginPosition(Long originPosition) {
        this.originPosition = originPosition;
    }

    public Long getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Long targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Position getOriginP() {
        return originP;
    }

    public void setOriginP(Position originP) {
        this.originP = originP;
    }

    public Position getTargetP() {
        return targetP;
    }

    public void setTargetP(Position targetP) {
        this.targetP = targetP;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}