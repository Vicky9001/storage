package com.example.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Task.TaskRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskRecordMapper extends BaseMapper<TaskRecord> {

}
