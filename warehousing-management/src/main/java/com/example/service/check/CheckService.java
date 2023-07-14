package com.example.service.check;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Task.*;
import com.example.entity.User.User;
import com.example.mapper.check.CheckMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckService {

    @Resource
    private CheckMapper checkMapper;
    @Resource
    RecordService recordService;
    @Resource
    PositionService positionService;
    @Resource
    ExternalClient externalClient;

    public List<CheckRecord> getList(List<Integer> type, Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        if(id!=null){
            wrapper.eq("creator_id",id);
        }
        if(type!=null){
            wrapper.in("state",type);
        }
        List<CheckRecord> list = checkMapper.selectList(wrapper);
        for (CheckRecord  p: list) {
            //查询该盘点单对应任务记录
            List<TaskRecord> task = recordService.getTask(6,p.getId());
            task.stream().map(t -> {
                User u = externalClient.getUser(t.getOperatorId());
                t.setUser(u);
                return t;
            }).collect(Collectors.toList());
            p.setTask(task);
        }
        return list;
    }

    public String changeState(Integer state, Long id, Long operator, String remark) {
        //获取盘点单信息，更改状态,重新插入
        CheckRecord checkRecord = checkMapper.selectById(id);
        checkRecord.setState(state);
        String res = null;
        TaskRecord task = new TaskRecord(6,checkRecord.getId(),operator,new Date(),remark);
        switch (state) {
            case 1:
                task.setTaskType(21);
                res = "盘点任务创建成功";
                break;
            case 2:
                task.setTaskType(22);
                res = "盘点数据录入成功";
                break;
            case 3:
                task.setTaskType(23);
                res = "盘点单审核通过";
                finish(checkRecord);
                break;
            case 4:
                task.setTaskType(24);
                res = "盘点单审核未通过，退回";
                break;
        }
        int f1 = checkMapper.updateById(checkRecord);
        //生成相应操作记录
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0)return null;
        return res;
    }

    // 选择一个仓位并创建盘点任务
    public CheckRecord create(Long creatorId, Long p, String desc) {
        CheckRecord record = new CheckRecord();
        Position position = positionService.findPosition(p);
        GoodsBatch goodsBatch = recordService.getGoodsBatchById(position.getBatchInfo());
        Goods goods = externalClient.findGoodsById(position.getGoodsType());
        //生成盘点单并插入
        User u = externalClient.getUser(creatorId);
        record.setCreatorId(creatorId);
        record.setCreatorName(u.getRealName());
        record.setTime(new Date());
        record.setState(1);
        record.setPositionId(p);
        record.setRecordId(goodsBatch.getRecordId());
        record.setRecordType(goodsBatch.getRecordType());
        record.setBatchInfo(goodsBatch.getId());
        record.setGoodsName(goods.getGoodsName());
        record.setUnit(goods.getUnit());
        record.setUnitPrice(goodsBatch.getUnitPrice());
        record.setRecordNum(position.getGoodsNum());
        record.setDesc(desc);
        checkMapper.insert(record);
        return record;
    }

    //盘点单录入货位实际储量
    public CheckRecord update(Long orderId, Float realNum) {
        CheckRecord record = checkMapper.selectById(orderId);
        Position p = positionService.findPosition(record.getPositionId());
        if(p.getCapacity()<realNum) return null;
        record.setRealNum(realNum);
        record.setDiffNum(record.getRealNum() - record.getRecordNum());
        record.setDiffPrice(record.getDiffNum()*record.getUnitPrice());
        checkMapper.updateById(record);
        return record;
    }

    //更改当前货位货物数量
    void finish(CheckRecord record){
        Position p = positionService.findPosition(record.getPositionId());
        if (record.getRealNum() == null) {
            record.setRealNum(0f);
        }
        p.setGoodsNum(record.getRealNum());
        if(record.getRealNum()==0) {
            p.setGoodsType(null);
            p.setState(0);
            p.setManufactureDate(null);
            p.setExpirationDate(null);
            p.setBatchInfo(null);
        }
        positionService.updatePositions(Arrays.asList(p));
    }
}
