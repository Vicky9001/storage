package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

@TableName("waiting_record")
public class WaitingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    @TableField(exist = false)
    private List<GoodsBatch> goodsBatches;

    public WaitingRecord() {
    }

    public WaitingRecord(Date time) {
        this.time = time;
    }

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

    public List<GoodsBatch> getGoodsBatches() {
        return goodsBatches;
    }

    public void setGoodsBatches(List<GoodsBatch> goodsBatches) {
        this.goodsBatches = goodsBatches;
    }
}
