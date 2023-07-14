package com.example.mapper.stokeOut;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Task.OutRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutMapper extends BaseMapper<OutRecord> {
}
