package com.example.service.stokeOut;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.Department;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Logistics;
import com.example.entity.Info.Position;
import com.example.entity.Statistics.InAndOut;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.OutRecord;
import com.example.entity.Task.TaskRecord;
import com.example.entity.User.User;
import com.example.mapper.info.InAndOutMapper;
import com.example.mapper.stokeOut.OutMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OutService {

    @Resource
    private OutMapper outMapper;
    @Resource
    private InAndOutMapper inAndOutMapper;
    @Resource
    RecordService recordService;
    @Resource
    PositionService positionService;
    @Resource
    ExternalClient externalClient;


    public List<OutRecord> getList(List<Integer> type) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("state",type);
        wrapper.orderByAsc("state");
        wrapper.orderByDesc("time");
        List<OutRecord> list = outMapper.selectList(wrapper);
        for (OutRecord  p: list) {
            //查询该出库单对应物流批次
            List<GoodsBatch> g = recordService.getGoods(3,p.getId());
            //货物详细信息获取
            g.stream().map(batch -> {
                Goods goods = externalClient.findGoodsById(batch.getGoodsId());
                batch.setGoods(goods);
                return batch;
            }).collect(Collectors.toList());
            p.setGoods(g);
            //查询该出库单对应任务记录
            List<TaskRecord> task = recordService.getTask(3,p.getId());
            task.stream().map(t -> {
                User u = externalClient.getUser(t.getOperatorId());
                t.setUser(u);
                return t;
            }).collect(Collectors.toList());
            p.setTask(task);
        }
        return list;
    }

    public OutRecord create(OutRecord outRecord) {
        outRecord.setTime(new Date());
        outRecord.setState(1);
        // 从库中挑选存储该物资的货位（按日期排序）
        List<Position> pl = positionService.findOutPositions(outRecord.getGoodsId(),1);
        Float delNum = outRecord.getNum();
        Float totalStorage = 0.0f;
        for (Position p : pl) {
            totalStorage += p.getGoodsNum();
        }
        System.out.println(totalStorage);
        //仓库库存货物不足
        if (totalStorage < delNum) {
            outRecord.setRemark("该物资仓库库存不足，无法执行当前任务，请联系管理员");
            return outRecord;
        }
        outMapper.insert(outRecord);
        //计算货物价格和物流价格
        float goodsPrice = 0;
        float logisticsPrice = 0;
        //减少货位库存量
        ArrayList<Position> updatePositions = new ArrayList<>();
        for (Position p : pl) {
            if(delNum==0) break;
            //生成物资批次
            GoodsBatch originG = recordService.getGoodsBatchById(p.getBatchInfo());
            GoodsBatch g = new GoodsBatch();
            g.setGoodsId(originG.getGoodsId());
            g.setRecordId(outRecord.getId());
            g.setRecordType(3);
            g.setRecordId(outRecord.getId());
            g.setManufactureDate(originG.getManufactureDate());
            g.setExpirationDate(originG.getExpirationDate());
            g.setUnitWeight(originG.getUnitWeight());
            g.setUnitPrice(originG.getUnitPrice());
            g.setSupplierId(originG.getSupplierId());
            g.setRelatedId(originG.getId());
            g.setPositionId(p.getPositionId());
            g.setInDate(p.getInDate());
            g.setOutDate(new Date());
            //更改出库货位的状态
            if(delNum>p.getGoodsNum()){
                g.setNum(p.getGoodsNum());
                p.setGoodsType(null);
                p.setState(0);
                p.setGoodsNum((float) 0);
                p.setManufactureDate(null);
                p.setExpirationDate(null);
                p.setBatchInfo(null);
                p.setInDate(null);
            }else{
                g.setNum(delNum);
                p.setGoodsNum(p.getGoodsNum()-delNum);
            }
            g.setTotalWeight(g.getUnitWeight()*g.getNum());
            g.setTotalPrice(g.getUnitPrice()*g.getNum());
            goodsPrice+=g.getTotalPrice();
            logisticsPrice+=g.getTotalWeight()*externalClient.findExpense(outRecord.getLogisticsType());
            updatePositions.add(p);
            recordService.insertGoods(g);
            delNum -= g.getNum();
        }
        //价格
        outRecord.setLogisticsPrice((double) logisticsPrice);
        outRecord.setGoodsPrice((double) goodsPrice);
        outRecord.setTotalPrice((double) (logisticsPrice+goodsPrice));
        outMapper.updateById(outRecord);
        positionService.updatePositions(updatePositions);
        //生成操作记录
        TaskRecord task = new TaskRecord(3,15,outRecord.getId(),outRecord.getCreatorId(),new Date(),outRecord.getRemark());
        recordService.insertTask(task);
        return outRecord;
    }

    public String changeState(Integer state, Long id, Long operator, String remark) {
        //获取移库单信息，更改状态,重新插入
        OutRecord outRecord = outMapper.selectById(id);
        outRecord.setRemark(remark);
        String res = null;
        switch (state) {
            case 16:
                outRecord.setState(2);
                res = "出库拣货确认";
                break;
            case 17:
                outRecord.setState(3);
                res = "复核确认";
                break;
            case 18:
                outRecord.setState(4);
                res = "打包完成确认";
                break;
            case 19:
                outRecord.setState(5);
                res = "出库完成确认";
                break;
            case 20:
                outRecord.setState(6);
                //物资批次挪到暂存区
                res = putIntoStagingArea(id);
                if(res!="不予出库") return res;
        }
        TaskRecord task = new TaskRecord(3,state,outRecord.getId(),operator,new Date(),outRecord.getRemark());
        int f1 = outMapper.updateById(outRecord);
        //生成相应操作记录
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0)return null;
        return res;
    }

    private String putIntoStagingArea(Long id) {
        OutRecord outRecord = outMapper.selectById(id);
        List<GoodsBatch> goodsBatch = recordService.getGoods(3, id);
        Goods goods = externalClient.findGoodsById(outRecord.getGoodsId());
        List<Position> stagingList = positionService.findPositions(null,0, goods.getGoodsType(), 0);
        Float totalCapacity = 0.0f;
        for (Position s : stagingList) {
            totalCapacity += s.getCapacity();
        }
        System.out.println(totalCapacity);
        //仓库余位不足
        if (totalCapacity < outRecord.getNum()) {
            return "暂存区仓库余位不足，无法执行当前任务，请联系管理员";
        }
        //把物资移到新的库位
        List<Position> newPositions = new ArrayList<>();
        int index = 0;
        for (GoodsBatch g : goodsBatch){
            Position newP = stagingList.get(index);
            //更改暂存区货位的状态
            newP.setGoodsType(g.getGoodsId());
            newP.setState(1);
            newP.setGoodsNum(g.getNum());
            newP.setManufactureDate(g.getManufactureDate());
            newP.setExpirationDate(g.getExpirationDate());
            newP.setBatchInfo(g.getId());
            newP.setInDate(g.getInDate());
            newPositions.add(newP);
            //更改物资批次库位跟出库时间
            g.setPositionId(newP.getPositionId());
            g.setOutDate(null);
            recordService.updateGoodsBatch(g);
            index++;
        }
        positionService.updatePositions(newPositions);
        return "不予出库";
    }

    public List<InAndOut> statistics() {
        QueryWrapper q1 = new QueryWrapper();
        q1.eq("time",new java.sql.Date(new Date().getTime()));
        q1.eq("type",3);
        InAndOut i;
        if(inAndOutMapper.exists(q1)) {
            i = inAndOutMapper.selectOne(q1);
        } else {
            i = new InAndOut();
            i.setTime(new java.sql.Date(new Date().getTime()));
            i.setType(3);
            inAndOutMapper.insert(i);
        }
        i.setGoodsNum(0.0f);
        i.setGoodsPrice(0.0);
        i.setLogisticsPrice(0.0);
        i.setTotalPrice(0.0);
        QueryWrapper q2 = new QueryWrapper();
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN); // 当天开始时间
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX); // 当天结束时间
        q2.between("update_time", startOfDay, endOfDay);
        q2.eq("state",5);
        List<OutRecord> w = outMapper.selectList(q2);
        for(OutRecord wr:w) {
            List<GoodsBatch> gbl = recordService.getGoods(3,wr.getId());
            for(GoodsBatch gb:gbl) {
                i.setGoodsNum(i.getGoodsNum()+gb.getNum());
            }
            i.setLogisticsPrice(i.getLogisticsPrice()+wr.getLogisticsPrice());
            i.setGoodsPrice(i.getGoodsPrice()+wr.getGoodsPrice());
        }
        i.setTotalPrice(i.getTotalPrice()+i.getLogisticsPrice()+i.getGoodsPrice());
        inAndOutMapper.updateById(i);

        QueryWrapper q3 = new QueryWrapper();
        q3.eq("type",3);
        List<InAndOut> list = inAndOutMapper.selectList(q3);
        for(InAndOut a:list) {
            QueryWrapper q4 = new QueryWrapper();
            LocalDateTime dateTime = LocalDateTime.ofInstant(a.getTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime start = dateTime.with(LocalTime.MIN); // 当天开始时间
            LocalDateTime end = dateTime.with(LocalTime.MAX); // 当天结束时间
            q4.between("update_time", start, end);
            q4.eq("state",5);
            List<OutRecord> r = outMapper.selectList(q4);
            Map<Long, Goods> gMap = new HashMap<>();
            Map<Integer, Logistics> lMap = new HashMap<>();
            Map<Long, Department> dMap = new HashMap<>();
            for(OutRecord or:r) {
                List<GoodsBatch> gbl = recordService.getGoods(3,or.getId());
                for(GoodsBatch gb:gbl) {
                    Long goodsId = gb.getGoodsId();
                    Goods goods = gMap.get(goodsId);
                    if (goods == null) {
                        goods = externalClient.findGoodsById(gb.getGoodsId());
                        goods.setGroundingNum(gb.getNum());
                        goods.setPrice(gb.getTotalPrice());
                        gMap.put(goodsId, goods);
                    } else {
                        goods.setPrice(goods.getPrice()+gb.getTotalPrice());
                        goods.setGroundingNum(goods.getGroundingNum()+gb.getNum());
                    }
                }
                Integer logisticType = or.getLogisticsType();
                Logistics logistics = lMap.get(logisticType);
                if (logistics == null) {
                    logistics = externalClient.findLogistics(logisticType);
                    logistics.setPrice(or.getLogisticsPrice());
                    lMap.put(logisticType, logistics);
                } else {
                    logistics.setPrice(logistics.getPrice()+or.getLogisticsPrice());
                }
                Long departmentId = or.getDepartmentId();
                Department department = dMap.get(departmentId);
                if (department == null) {
                    department = externalClient.findDepartment(departmentId);
                    department.setNum(or.getNum());
                    dMap.put(departmentId, department);
                } else {
                    department.setNum(department.getNum()+or.getNum());
                }
            }
            List<Goods> gl = new ArrayList<>(gMap.values());
            List<Logistics> ll = new ArrayList<>(lMap.values());
            List<Department> dl = new ArrayList<>(dMap.values());
            a.setGoods(gl);
            a.setLogistics(ll);
            a.setDepartments(dl);
        }
        return list;
    }

    public Long getOutNum() {
        QueryWrapper q1 = new QueryWrapper();
        System.out.println(new java.sql.Date(new Date().getTime()));
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        q1.eq("state",5);
        q1.between("update_time", startDateTime, endDateTime);
        return outMapper.selectCount(q1);
    }

    public Double getPrice() {
        QueryWrapper q = new QueryWrapper();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        q.between("update_time", startDateTime, endDateTime);
        q.select("ifnull(sum(total_price),0) as totalPrice");
        Double sum = outMapper.selectOne(q).getTotalPrice();
        return sum;
    }
}