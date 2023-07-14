package com.example.service.transfer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Task.*;
import com.example.entity.User.User;
import com.example.mapper.transfer.TransferMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Resource
    private TransferMapper transferMapper;
    @Resource
    RecordService recordService;
    @Resource
    PositionService positionService;
    @Resource
    ExternalClient externalClient;


    public List<TransferRecord> getList(List<Integer> type, Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        if(id!=null){
            wrapper.eq("creator_id",id);
        }
        wrapper.in("state",type);
        wrapper.orderByAsc("state");
        wrapper.orderByDesc("time");
        List<TransferRecord> list = transferMapper.selectList(wrapper);
        for (TransferRecord  p: list) {
            //查询该内配单对应物流批次
            List<GoodsBatch> g = recordService.getGoods(4,p.getId());
            //货物详细信息获取
            g.stream().map(batch -> {
                Goods goods = externalClient.findGoodsById(batch.getGoodsId());
                batch.setGoods(goods);
                return batch;
            }).collect(Collectors.toList());
            p.setGoods(g);
            //查询该内配单对应任务记录
            List<TaskRecord> task = recordService.getTask(4,p.getId());
            task.stream().map(t -> {
                User u = externalClient.getUser(t.getOperatorId());
                t.setUser(u);
                return t;
            }).collect(Collectors.toList());
            p.setTask(task);
            //查询该内配单对应原位置信息和现在位置信息
            Position originP = positionService.findPosition(p.getOriginPosition());
            Position targetP = positionService.findPosition(p.getTargetPosition());
            p.setOriginP(originP);
            p.setTargetP(targetP);
        }
        return list;
    }

    public String changeState(Integer state, Long id, Long operator, String remark) {
        //获取移库单信息，更改状态,重新插入
        TransferRecord transferRecord = transferMapper.selectById(id);
        transferRecord.setRemark(remark);
        String res = null;
        TaskRecord task = new TaskRecord(4,state,transferRecord.getId(),operator,new Date(),transferRecord.getRemark());
        switch (state) {
            case 12:
                transferRecord.setState(2);
                res = "移库单审核通过";
                break;
            case 13:
                transferRecord.setState(3);
                res = "移库单审核未通过，退回";
                break;
            case 14:
                transferRecord.setState(4);
                finish(transferRecord);
                res = "货物转移上架成完成";
        }
        int f1 = transferMapper.updateById(transferRecord);
        //生成相应操作记录
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0)return null;
        return res;
    }

    public String create(Long creatorId, Long originP, Integer targetArea, Float delNum,String desc) {
        TransferRecord record = new TransferRecord();
        Position originPosition = positionService.findPosition(originP);
        GoodsBatch goodsBatch = recordService.getGoodsBatchById(originPosition.getBatchInfo());
        Goods goods = externalClient.findGoodsById(originPosition.getGoodsType());
        List<Position> spare = positionService.findPositions(null,targetArea,goods.getGoodsType(),0);
        if(spare==null){
            return "该货区仓库余位不足，无法执行当前任务，请联系管理员";
        }
        //修改原物资批次的出库时间
        goodsBatch.setOutDate(new Date());
        recordService.updateGoodsBatch(goodsBatch);
        //生成内配单并插入
        Position targetPosition = spare.get(0);//选取目标库位
        User u = externalClient.getUser(creatorId);
        record.setCreatorId(creatorId);
        record.setCreatorName(u.getRealName());
        record.setTime(new Date());
        record.setState(1);
        record.setOriginPosition(originP);
        record.setTargetPosition(targetPosition.getPositionId());
        record.setNum(delNum);
        record.setDesc(desc);
        int f1 = transferMapper.insert(record);
        //生成物资批次
        GoodsBatch g = new GoodsBatch();
        g.setGoodsId(originPosition.getGoodsType());
        g.setRecordType(4);
        g.setRecordId(record.getId());
        g.setNum(delNum);
        g.setManufactureDate(goodsBatch.getManufactureDate());
        g.setExpirationDate(goodsBatch.getExpirationDate());
        g.setUnitWeight(goodsBatch.getUnitWeight());
        g.setUnitPrice(goodsBatch.getUnitPrice());
        g.setTotalPrice(g.getUnitPrice()*g.getNum());
        g.setTotalWeight(g.getUnitWeight()*g.getNum());
        g.setSupplierId(goodsBatch.getSupplierId());
        g.setRelatedId(goodsBatch.getId());
        g.setPositionId(targetPosition.getPositionId());
        recordService.insertGoodsBatch(g);
        //生成操作记录
        TaskRecord task = new TaskRecord(11,record.getId(),record.getCreatorId(),new Date(),record.getRemark());
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0) return "操作失败";
        return "内配单创建成功";
    }

    void finish(TransferRecord record){
        Long originP = record.getOriginPosition();
        Long targetP = record.getTargetPosition();
        Float delNum = record.getNum();
        ArrayList<Position> pl = new ArrayList<>();
        Position originPosition = positionService.findPosition(originP);
        Position targetPosition = positionService.findPosition(targetP);
        //修改目标库位信息
        targetPosition.setGoodsType(originPosition.getGoodsType());
        targetPosition.setState(1);
        targetPosition.setGoodsNum(delNum);
        targetPosition.setManufactureDate(originPosition.getManufactureDate());
        targetPosition.setExpirationDate(originPosition.getExpirationDate());
        targetPosition.setBatchInfo(originPosition.getBatchInfo());
        targetPosition.setInDate(originPosition.getInDate());
        pl.add(targetPosition);
        //修改原库位信息
        originPosition.setGoodsNum(originPosition.getGoodsNum()-delNum);
        if(originPosition.getGoodsNum()==0){
            originPosition.setGoodsType(null);
            originPosition.setState(0);
            originPosition.setManufactureDate(null);
            originPosition.setExpirationDate(null);
            originPosition.setBatchInfo(null);
            originPosition.setInDate(null);
        }
        pl.add(originPosition);
        positionService.updatePositions(pl);
    }

    public Long getTransferNum() {
        QueryWrapper q1 = new QueryWrapper();
        System.out.println(new java.sql.Date(new java.util.Date().getTime()));
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println("开始时间:" +startDateTime + " 结束时间:" + endDateTime);
        q1.eq("state",4);
        q1.between("update_time", startDateTime, endDateTime);
        return transferMapper.selectCount(q1);
    }
}
