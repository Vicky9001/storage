package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Info.Goods;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("warning_record")
public class WarningRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long warningId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date time;
    private Integer type;
    private Long positionId;
    private Long goodsId;
    private Float num;
    private Float extraNum;
    private Integer extraTime;
    private Integer state;
    @TableField(exist = false)
    private Goods goods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarningId() {
        return warningId;
    }

    public void setWarningId(Long warningId) {
        this.warningId = warningId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Float getExtraNum() {
        return extraNum;
    }

    public void setExtraNum(Float extraNum) {
        this.extraNum = extraNum;
    }

    public Integer getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(Integer extraTime) {
        this.extraTime = extraTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
