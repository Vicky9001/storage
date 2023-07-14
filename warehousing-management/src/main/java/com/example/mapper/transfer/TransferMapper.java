package com.example.mapper.transfer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Task.TransferRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferMapper extends BaseMapper<TransferRecord> {

}
