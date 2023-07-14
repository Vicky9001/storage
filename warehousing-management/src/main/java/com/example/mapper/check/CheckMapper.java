package com.example.mapper.check;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Task.CheckRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckMapper extends BaseMapper<CheckRecord> {

}
