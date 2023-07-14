package com.example.mapper.warning;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Task.Warning;
import com.example.entity.Task.WarningRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarningRecordMapper extends BaseMapper<WarningRecord> {

}
