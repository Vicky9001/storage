package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.TaskRecord;
import com.example.mapper.info.GoodsBatchMapper;
import com.example.mapper.info.TaskRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordService {
    @Resource
    private GoodsBatchMapper goodsBatchMapper;
    @Resource
    private TaskRecordMapper taskRecordMapper;


    public Integer delete(Integer recordType, Long recordId) {
        QueryWrapper<GoodsBatch> queryWrapper = new QueryWrapper<>();
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("record_type", recordType);
        queryParamsMap.put("record_id", recordId);
        queryWrapper.allEq(queryParamsMap);
        return goodsBatchMapper.delete(queryWrapper);
    }

    public Integer insertGoods(GoodsBatch g) {
        return goodsBatchMapper.insert(g);
    }

    public GoodsBatch insertGoodsBatch(GoodsBatch g) {
        goodsBatchMapper.insert(g);
        return g;
    }

    public Integer updateGoodsBatch(GoodsBatch g) {
        return goodsBatchMapper.updateById(g);
    }

    public Integer insertTask(TaskRecord t) {
        return taskRecordMapper.insert(t);
    }

    public List<GoodsBatch> getGoods(Integer recordType, Long recordId) {
        QueryWrapper w = new QueryWrapper();
        Map<String, Object> qm1 = new HashMap<>();
        qm1.put("record_type", recordType);
        qm1.put("record_id", recordId);
        w.allEq(qm1);
        w.orderByAsc("record_type");
        return goodsBatchMapper.selectList(w);
    }

    public List<TaskRecord> getTask(Integer recordType, Long recordId) {
        QueryWrapper w = new QueryWrapper();
        Map<String, Object> qm2 = new HashMap<>();
        if(recordType!=null) qm2.put("record_type", recordType);
        if(recordId!=null) qm2.put("record_id", recordId);
        w.allEq(qm2);
        w.orderByAsc("record_type");
        return taskRecordMapper.selectList(w);
    }

    //通过订单类型、供应商id、货物id查找
    public List<GoodsBatch> findGoodsList(Integer recordType, Long supplierId, Long goodsId) {
        QueryWrapper w = new QueryWrapper();
        Map<String, Object> qm3 = new HashMap<>();
        if(recordType!=null) qm3.put("record_type", recordType);
        if(supplierId!=null) qm3.put("supplier_id", supplierId);
        if(goodsId!=null) qm3.put("goods_id", goodsId);
        w.allEq(qm3);
        return goodsBatchMapper.selectList(w);
    }

    public GoodsBatch getGoodsBatchById(Long id) {
        return goodsBatchMapper.selectById(id);
    }
}
