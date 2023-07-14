package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


@TableName("check_record")//当实体类和表名不一致的时候，用@tablename来指定表名
public class CheckRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long creatorId;
    private String creatorName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private Integer state;
    @TableField(value = "`desc`")
    private String desc;
    private Long positionId;
    private Long recordId;
    private Integer recordType;
    private Long batchInfo;
    private String goodsName;
    private String unit;
    private Float unitPrice;
    private Float recordNum;
    private Float realNum;
    private Float diffNum;
    private Float diffPrice;
    @TableField(exist = false)
    List<TaskRecord> task;

    public CheckRecord() {
    }

    public CheckRecord(Long id, Long creatorId, String creatorName, Date time, Integer state, String desc, Long positionId, Long recordId, Integer recordType, Long batchInfo, String goodsName, String unit, Float unitPrice, Float recordNum, Float realNum, Float diffNum, Float diffPrice, List<TaskRecord> task) {
        this.id = id;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.time = time;
        this.state = state;
        this.desc = desc;
        this.positionId = positionId;
        this.recordId = recordId;
        this.recordType = recordType;
        this.batchInfo = batchInfo;
        this.goodsName = goodsName;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.recordNum = recordNum;
        this.realNum = realNum;
        this.diffNum = diffNum;
        this.diffPrice = diffPrice;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Long getBatchInfo() {
        return batchInfo;
    }

    public void setBatchInfo(Long batchInfo) {
        this.batchInfo = batchInfo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(Float recordNum) {
        this.recordNum = recordNum;
    }

    public Float getRealNum() {
        return realNum;
    }

    public void setRealNum(Float realNum) {
        this.realNum = realNum;
    }

    public Float getDiffNum() {
        return diffNum;
    }

    public void setDiffNum(Float diffNum) {
        this.diffNum = diffNum;
    }

    public Float getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(Float diffPrice) {
        this.diffPrice = diffPrice;
    }

    public List<TaskRecord> getTask() {
        return task;
    }

    public void setTask(List<TaskRecord> task) {
        this.task = task;
    }
}