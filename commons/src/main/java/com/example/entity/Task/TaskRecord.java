package com.example.entity.Task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.User.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


@TableName("task_record")//当实体类和表名不一致的时候，用@tablename来指定表名
public class TaskRecord {
    @TableId(type = IdType.AUTO)
    private Long taskId;
    private Integer recordType;
    private Long recordId;
    private Integer taskType;
    private Long operatorId;
    private String remark;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operateTime;
    @TableField(exist = false)
    private User user;
    public TaskRecord(){
    }
    public TaskRecord(Integer recordType,Long id, Long operator, Date date, String remark) {
        this.recordType = recordType;
        this.recordId = id;
        this.operatorId = operator;
        this.operateTime = date;
        this.remark = remark;
    }

    public TaskRecord(Integer recordType,Integer taskType, Long recordId,  Long operatorId, Date operateTime, String remark) {
        this.recordType = recordType;
        this.taskType = taskType;
        this.recordId = recordId;
        this.operatorId = operatorId;
        this.operateTime = operateTime;
        this.remark = remark;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TaskRecord{" +
                "taskId=" + taskId +
                ", recordId=" + recordId +
                ", taskType=" + taskType +
                ", operatorId=" + operatorId +
                ", remark='" + remark + '\'' +
                ", operateTime=" + operateTime +
                ", user=" + user +
                '}';
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}