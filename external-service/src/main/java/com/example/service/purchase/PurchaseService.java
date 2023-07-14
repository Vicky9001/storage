package com.example.service.purchase;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.*;
import com.example.entity.Info.Goods;
import com.example.entity.Info.GoodsSupplier;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.PurchaseRecord;
import com.example.entity.Task.TaskRecord;
import com.example.entity.User.User;
import com.example.mapper.purchase.PurchaseMapper;
import com.example.service.info.DepartmentService;
import com.example.service.info.GoodsService;
import com.example.service.info.LogisticsService;
import com.example.service.record.RecordService;
import com.example.service.sys.UserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    RecordService recordService;
    @Resource
    GoodsService goodsService;
    @Resource
    LogisticsService logisticsService;
    @Resource
    UserService userService;
    @Resource
    DepartmentService departmentService;
    @Resource
    WarehouseClient warehouseClient;

    public PurchaseRecord getPurchase(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id",id);
        PurchaseRecord p = purchaseMapper.selectOne(wrapper);
        //查询该采购单对应物流批次warehouse
        List<GoodsBatch> g = recordService.getGoods(1,p.getId());
        //货物详细信息获取
        g.stream().map(batch -> {
            Goods goods = goodsService.getGoods(batch.getGoodsId());
            batch.setGoods(goods);
            return batch;
        }).collect(Collectors.toList());
        p.setGoods(g);
        //查询该采购单对应任务记录
        List<TaskRecord> task = recordService.getTask(1,p.getId());
        task.stream().map(t -> {
            User u = userService.getUser(t.getOperatorId());
            t.setUser(u);
            return t;
        }).collect(Collectors.toList());
        p.setTask(task);
        return p;
    }

    public List<PurchaseRecord> getList(List<Integer> type, Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        if(id!=null){
            wrapper.eq("creator_id",id);
        }
        for(Integer state:type) {
            if (state != 1 && state != 2 && state != 3 && state != 4 && state != 5) {
                throw new IllegalArgumentException("Invalid state: " + state);
            }
        }
        wrapper.in("state",type);
        wrapper.orderByAsc("state");
        wrapper.orderByDesc("time");
        List<PurchaseRecord> list = purchaseMapper.selectList(wrapper);
        for (PurchaseRecord  p: list) {
            //查询该采购单对应物流批次
            List<GoodsBatch> g = recordService.getGoods(1,p.getId());
            //货物详细信息获取
            g.stream().map(batch -> {
                Goods goods = goodsService.getGoods(batch.getGoodsId());
                batch.setGoods(goods);
                return batch;
            }).collect(Collectors.toList());
            p.setGoods(g);
            //查询该采购单对应任务记录
            List<TaskRecord> task = recordService.getTask(1,p.getId());
            task.stream().map(t -> {
                User u = userService.getUser(t.getOperatorId());
                t.setUser(u);
                return t;
            }).collect(Collectors.toList());
            p.setTask(task);
        }
        return list;
    }

    public PurchaseRecord addPurchase(PurchaseRecord purchaseRecord) {
        //插入数据库获取采购单id
        int f1 = purchaseMapper.insert(purchaseRecord);
        if (f1<0)return null;
        float goodsPrice = 0;
        float logisticsPrice = 0;
        for(GoodsBatch g:purchaseRecord.getGoods()){
            //生成相应批次货物记录
            g.setRecordType(1);
            g.setRecordId(purchaseRecord.getId());
            g.setSupplierId(purchaseRecord.getSupplierId());
            //获取该供应商提供货物的单位价钱和重量，计算货物费用和物流费用
            GoodsSupplier info = goodsService.getGoodsSupplier(purchaseRecord.getSupplierId(),g.getGoodsId());
            g.setUnitPrice(info.getUnitPrice());
            g.setTotalPrice(g.getNum()*g.getUnitPrice());
            g.setUnitWeight(info.getUnitWeight());
            g.setTotalWeight(g.getNum()*g.getUnitWeight());
            goodsPrice+=g.getTotalPrice();
            logisticsPrice+=g.getTotalWeight()*logisticsService.getExpense(purchaseRecord.getLogisticsType());
            recordService.insertGoodsBatch(g);
            return null;
        }
        //更新采购单信息重新插入
        User u = userService.getUser(purchaseRecord.getCreatorId());
        purchaseRecord.setTime(new Date());
        purchaseRecord.setState(1);
        purchaseRecord.setCreatorName(u.getRealName());
        purchaseRecord.setLogisticsPrice((double) logisticsPrice);
        purchaseRecord.setGoodsPrice((double) goodsPrice);
        purchaseRecord.setTotalPrice((double) (logisticsPrice+goodsPrice));
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,1,purchaseRecord.getId(),purchaseRecord.getCreatorId(),new Date(),purchaseRecord.getRemark());
        int f3 = purchaseMapper.updateById(purchaseRecord);
        int f4 = recordService.insertTask(task);
        if(f3<0||f4<0)return null;
        return purchaseRecord;
    }

    public PurchaseRecord approve(Long id,String remark,Long operator) {
        //获取采购单信息，更改状态,重新插入
        PurchaseRecord purchaseRecord = purchaseMapper.selectById(id);
        purchaseRecord.setState(2);
        purchaseRecord.setRemark(remark);
        int f1 = purchaseMapper.updateById(purchaseRecord);
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,3,purchaseRecord.getId(),operator,new Date(),purchaseRecord.getRemark());
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0)return null;
        return purchaseRecord;
    }

    public PurchaseRecord reject(Long id,String remark,Long operator) {
        //获取采购单信息，更改状态,重新插入
        PurchaseRecord purchaseRecord = purchaseMapper.selectById(id);
        purchaseRecord.setState(3);
        purchaseRecord.setRemark(remark);
        int f1 = purchaseMapper.updateById(purchaseRecord);
        User f2 = userService.getUser(operator);
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,4,purchaseRecord.getId(),operator,new Date(),purchaseRecord.getRemark());
        int f3 = recordService.insertTask(task);
        if(f1<0||f2==null||f3<0) return null;
        return purchaseRecord;
    }


    public PurchaseRecord update(PurchaseRecord purchaseRecord) {
        //删除之前对应表单id的物资批次
        int f1 = recordService.delete(1,purchaseRecord.getId());
        if(f1<0) return null;
        //插入新的物资批次
        float goodsPrice = 0;
        float logisticsPrice = 0;
        for(GoodsBatch g:purchaseRecord.getGoods()){
            g.setRecordType(1);
            g.setRecordId(purchaseRecord.getId());
            g.setSupplierId(purchaseRecord.getSupplierId());
            //获取该供应商提供货物的单位价钱和重量，计算货物费用和物流费用
            GoodsSupplier info = goodsService.getGoodsSupplier(purchaseRecord.getSupplierId(),g.getGoodsId());
            g.setUnitPrice(info.getUnitPrice());
            g.setTotalPrice(g.getNum()*g.getUnitPrice());
            g.setUnitWeight(info.getUnitWeight());
            g.setTotalWeight(g.getNum()*g.getUnitWeight());
            goodsPrice+=g.getTotalPrice();
            logisticsPrice+=g.getTotalWeight()*logisticsService.getExpense(purchaseRecord.getLogisticsType());
            recordService.insertGoodsBatch(g);
            return null;
        }
        //更新采购单信息重新插入
        User u = userService.getUser(purchaseRecord.getCreatorId());
        purchaseRecord.setTime(new Date());
        purchaseRecord.setState(1);
        purchaseRecord.setCreatorName(u.getRealName());
        purchaseRecord.setLogisticsPrice((double) logisticsPrice);
        purchaseRecord.setGoodsPrice((double) goodsPrice);
        purchaseRecord.setTotalPrice((double) (logisticsPrice+goodsPrice));
        int f3 = purchaseMapper.updateById(purchaseRecord);
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,2,purchaseRecord.getId(),purchaseRecord.getCreatorId(),new Date(),purchaseRecord.getRemark());
        int f4 = recordService.insertTask(task);
        if(f3<0||f4<0)return null;
        return purchaseRecord;
    }

    public PurchaseRecord reserve(Long id,String remark,Long operator) {
        //修改采购单状态
        PurchaseRecord purchaseRecord = purchaseMapper.selectById(id);
        purchaseRecord.setState(4);
        purchaseRecord.setRemark(remark);
        //为物资生成生产日期和有效期
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        c.setTime(date);//设置日历时间
        c.add(Calendar.DATE,30);
        Date mdate = c.getTime();
        c.add(Calendar.MONTH,24);//在日历的月份上增加多少月
        Date edate = c.getTime();
        List<GoodsBatch> goods = recordService.getGoods(1,purchaseRecord.getId());
        for(GoodsBatch g:goods){
            g.setManufactureDate(mdate);
            g.setExpirationDate(edate);
            int f1 = recordService.updateGoodsBatch(g);
            if(f1<0) return null;
        }
        //调用入库那里预约生成入库单
        Integer f2 = warehouseClient.createWaitingOrder(purchaseRecord.getId());
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,5,purchaseRecord.getId(),operator,new Date(),purchaseRecord.getRemark());
        int f3 = purchaseMapper.updateById(purchaseRecord);
        int f4 = recordService.insertTask(task);
        if(f2<0||f3<0||f4<0)return null;
        return purchaseRecord;
    }

    public Long getPurchaseNum() {
        QueryWrapper q1 = new QueryWrapper();
        System.out.println(new java.sql.Date(new java.util.Date().getTime()));
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println("开始时间:" +startDateTime + " 结束时间:" + endDateTime);
        q1.eq("state",4);
        q1.between("update_time", startDateTime, endDateTime);
        return purchaseMapper.selectCount(q1);
    }

    public Double getOrderPrice() {
        Double outPrice = warehouseClient.getOrderPrice();
        QueryWrapper q = new QueryWrapper();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        q.between("update_time", startDateTime, endDateTime);
        q.select("ifnull(sum(total_price),0) as totalPrice");
        Double sum = purchaseMapper.selectOne(q).getTotalPrice()+outPrice;
        return sum;
    }

    public PurchaseRecord pay(Long id, Long operator) {
        //获取采购单信息，更改状态,重新插入
        PurchaseRecord purchaseRecord = purchaseMapper.selectById(id);
        purchaseRecord.setState(5);
        int f1 = purchaseMapper.updateById(purchaseRecord);
        //生成相应操作记录
        TaskRecord task = new TaskRecord(1,21,purchaseRecord.getId(),operator,new Date(),purchaseRecord.getRemark());
        int f2 = recordService.insertTask(task);
        if(f1<0||f2<0)return null;
        return purchaseRecord;
    }
}
