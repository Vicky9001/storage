package com.example.service.stokeIn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.*;
import com.example.entity.Task.*;
import com.example.entity.User.User;
import com.example.entity.Statistics.InAndOut;
import com.example.mapper.info.InAndOutMapper;
import com.example.mapper.stokeIn.WaitingRecordMapper;
import com.example.service.info.*;
import com.example.service.info.RecordService;
import org.springframework.stereotype.Service;
import com.example.mapper.stokeIn.WarehousingMapper;


import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehousingService {

    @Resource
    private WarehousingMapper warehousingMapper;
    @Resource
    private WaitingRecordMapper waitingRecordMapper;
    @Resource
    private InAndOutMapper inAndOutMapper;
    @Resource
    RecordService recordService;
    @Resource
    AreaService areaService;
    @Resource
    PositionService positionService;
    @Resource
    ExternalClient externalClient;

    public List<Area> findAreaList(Long goodsId) {
        Integer goodsType = externalClient.findGoodsById(goodsId).getGoodsType();
        List<Area> areas =  areaService.getAreaByType(null, 1,goodsType);
        for(Area a:areas) {
            List<Position> pl = positionService.findPositions(a.getId(),1,goodsType,0);
            Float currCapacity = 0.0f;
            for (Position p:pl) {
                currCapacity += p.getCapacity();
            }
            a.setCurrCapacity(currCapacity);//仓库当前容量
        }
        return areas;
    }
    public Integer createWaitingOrder(Long recordId){
        // 创建待入库单
        WaitingRecord wr = new WaitingRecord(new Date());
        waitingRecordMapper.insert(wr);
        // 获取采购批次并创建新的批次，绑定待入库单
        List<GoodsBatch> goodsBatches = recordService.getGoods(1, recordId);
        for (GoodsBatch gb : goodsBatches) {
           GoodsBatch g = new GoodsBatch(null, gb.getGoodsId(), 5, wr.getId(), gb.getNum(), gb.getSupplierId(), gb.getManufactureDate(), gb.getExpirationDate(), gb.getUnitPrice(), gb.getTotalPrice(), gb.getUnitWeight(), gb.getTotalWeight());
           Integer res = recordService.insertGoods(g);
           if(res<=0)return 0;
        }
        return 1;
    }

    // 获取等待入库的每一批物资
    public List<GoodsBatch> getWaitingList(Long supplierId, Long goodsId) {
        // 返回待入库的所有批次
        List<GoodsBatch> goodsBatches =  recordService.findGoodsList(5,supplierId,goodsId);
        for(GoodsBatch goods:goodsBatches) {
            goods.setSupplier(externalClient.findSupplier(goods.getSupplierId()));
            goods.setGoods(externalClient.findGoodsById(goods.getGoodsId()));
        }
        return goodsBatches;
    }

    // 为多批物资创建入库单 需要同时请求修改状态1-待预检验收的接口
    public String addWarehousing(WarehousingRecord wr) {
        List<GoodsBatch> goodsBatches = wr.getGoodsBatches();
        List<String> warning = new ArrayList<>();
        for (GoodsBatch gb : goodsBatches) {
            GoodsBatch goodsBatch = recordService.getGoodsBatchById(gb.getId());
            Goods g = externalClient.findGoodsById(goodsBatch.getGoodsId());
            List<Position> pl = positionService.findPositions(gb.getAreaId(),null,g.getGoodsType(),0);
            Float num = gb.getDelnum();
            Float totalCapacity = 0.0f;
            for (Position p:pl) {
                totalCapacity += p.getCapacity();
            }
            if (num <= 0) {
                throw new IllegalArgumentException("物资名称：" + g.getGoodsName() + "，入库数量必须大于0");
            } else if (num > goodsBatch.getNum()) {
                throw new IllegalArgumentException("物资名称：" + g.getGoodsName() + "，入库数量不能大于待入库数量");
            } else if (Math.floor(num) != num) {
                throw new IllegalArgumentException("物资名称：" + g.getGoodsName() + "，入库数量必须为整数");
            } else if (totalCapacity < num) {
                warning.add("物资名称：" + g.getGoodsName());
            }
        }
        if(warning.size()>0){
            throw new RuntimeException("仓库余位不足，入库单创建失败，请修改物资入库数量或联系管理员\n" + String.join("\n",warning));
        }
        // 为每一批物资删除数据，并创建新的批次号，绑定上一批物资，增加对应数据
        User u = externalClient.getUser(wr.getCreatorId());
        wr.setTime(new Date());
        wr.setCreatorName(u.getRealName());
        wr.setCreatorId(u.getUserId());
        wr.setDesc(wr.getDesc());
        warehousingMapper.insert(wr);
        //获取待入库单
        List<GoodsBatch> ngb = new ArrayList<>();
        Double goodsPrice = 0.0;
        StringBuilder sb = new StringBuilder();
        sb.append("入库单创建成功！入库信息：\n");
        for (GoodsBatch gb : goodsBatches) {
            System.out.println(gb.getId());
            GoodsBatch goodsBatch = recordService.getGoodsBatchById(gb.getId());
            System.out.println(goodsBatch);
            goodsBatch.setNum(goodsBatch.getNum() - gb.getDelnum());
            recordService.updateGoodsBatch(goodsBatch);//修改绑定待入库单的物资数量
            //新建绑定入库单和待入库批次的物资批次
            GoodsBatch ng = recordService.insertGoodsBatch(new GoodsBatch(goodsBatch.getId(),goodsBatch.getGoodsId(), 2, wr.getId(), gb.getDelnum(), goodsBatch.getSupplierId(), goodsBatch.getManufactureDate(), goodsBatch.getExpirationDate(), goodsBatch.getUnitPrice(), goodsBatch.getUnitPrice()*gb.getDelnum(), goodsBatch.getUnitWeight(), goodsBatch.getUnitWeight()*gb.getDelnum()));
            String goodsName = externalClient.findGoodsById(ng.getGoodsId()).getGoodsName();
            List<Position> pl = positionService.findPositions(gb.getAreaId(),null,null,0);
            List<Position> pln = new ArrayList<>();
            Float num = ng.getNum();//入库数量
            goodsPrice += ng.getTotalPrice();
            for (Position p : pl) {
                Float capacity = p.getCapacity();
                if(num<=0) break;
                p.setGoodsType(ng.getGoodsId());
                p.setState(2);//仓位状态：入库预约
                p.setGoodsNum(num > capacity ? capacity : num);
                p.setManufactureDate(ng.getManufactureDate());
                p.setExpirationDate(ng.getExpirationDate());
                p.setBatchInfo(ng.getId());
                Area area = areaService.selectInfo(gb.getAreaId());
                String areaTypeStr;
                if(area.getAreaType() == 0) {
                    areaTypeStr = "暂存区";
                } else if(area.getAreaType() == 1) {
                    areaTypeStr = "普通存储区";
                } else {
                    areaTypeStr = "处理品区";
                }
                String info = "货物名称：" + goodsName + " 仓库名：" + area.getHouseName() + " 仓库类型：" + areaTypeStr + " 货架编号：" + p.getPositionId() + " 存放数量：" + p.getGoodsNum() + "\n";
                sb.append(info);
                pln.add(p);
                num -= capacity;
            }
            positionService.updatePositions(pln);
            ng.setPositionList(pln);
            ngb.add(ng);
        }
        wr.setGoodsBatches(ngb);
        wr.setGoodsPrice(goodsPrice);
        warehousingMapper.updateById(wr);
        TaskRecord t = new TaskRecord(2,5,wr.getId(), wr.getCreatorId(), new Date(), null);
        recordService.insertTask(t);
        System.out.println(sb.toString());
        return sb.toString();
    }

    // 获取入库单 绑定包括任务状态的各种信息
    public List<WarehousingRecord> getOrderList(List<Integer> state, Long id, Long goodsId, Long supplier) {
        // 根据state，入库单id， goods类型，supplier查找
        QueryWrapper<WarehousingRecord> wrapper = new QueryWrapper<>();
        if (state != null && !state.isEmpty()) {
            wrapper.in("state", state);
        }
        if (id != null) {
            wrapper.eq("id", id);
        }
        if (goodsId != null) {
            wrapper.inSql("id", "SELECT DISTINCT record_id FROM goods_batch WHERE goods_id = " + goodsId);
        }
        if (supplier != null) {
            wrapper.inSql("id", "SELECT DISTINCT record_id FROM goods_batch WHERE supplier_id = " + supplier);
        }
        wrapper.orderByAsc("state");
        wrapper.orderByDesc("time");
        List<WarehousingRecord> list = warehousingMapper.selectList(wrapper);
        for (WarehousingRecord wr : list) {
            //查询该入库单对应物资批次
            List<GoodsBatch> g = recordService.getGoods(2, wr.getId());
            //货物详细信息获取
            g.stream().map(batch -> {
                Goods goods = externalClient.findGoodsById(batch.getGoodsId());
                batch.setGoods(goods);
                batch.setSupplier(externalClient.findSupplier(batch.getSupplierId()));
                //货位详细信息获取
                List<Position> p = positionService.findPositionByGoods(batch.getId());
                if(p.size()>0){
                    batch.setPositionList(p);
                    Long areaId = p.get(0).getAreaId();
                    batch.setHouseName(areaService.getAreaByType(areaId,null,null).get(0).getHouseName());
                }
                return batch;
            }).collect(Collectors.toList());
            wr.setGoodsBatches(g);
            //查询该入库单对应任务记录
            List<TaskRecord> r = new ArrayList<>();
            List<TaskRecord> task = recordService.getTask(2,wr.getId());
            if(!task.isEmpty()) {
                task.stream().map(t -> {
                    User u = externalClient.getUser(t.getOperatorId());
                    t.setUser(u);
                    return t;
                }).collect(Collectors.toList());
                r.addAll(task);
            }
            wr.setTaskRecords(r);
        }
        return list;
    }

    // 修改订单状态 0   1待预检验收，  2已删除，  3待确认收货，        4审核不通过，5待入库上架，6已上架
    // 添加任务记录 5预约入库，6删除入库单，7验收确认，8确认收货，9入库审核未通过，       10入库上架
    public void changeState(Long orderId, Integer state, Long userId, String remark) {
        // 更新状态
        WarehousingRecord warehousingRecord = warehousingMapper.selectById(orderId);
        warehousingRecord.setState(state);
        warehousingMapper.updateById(warehousingRecord);
        TaskRecord t = new TaskRecord(2,warehousingRecord.getId(), userId, new Date(), remark);
        switch (state) {
            case 2:
                t.setTaskType(6);//（逻辑）删除用户单
                deleteOrder(orderId);
                break;
            case 3:
                t.setTaskType(7);//验收确认
                break;
            case 4:
                t.setTaskType(9);//入库审核未通过
                putIntoStagingArea(orderId);//取消2-入库预约，放入暂存区
                break;
            case 5:
                t.setTaskType(8);//确认收货
                break;
            case 6:
                t.setTaskType(10);//入库上架
                putIntoWareHouse(orderId);//将货区状态由2-入库预约改为1-已占用
        }
        recordService.insertTask(t);
    }

    // 删除记录
    public void deleteOrder(Long orderId) {
        // 遍历查找warehousingId绑定的物资批次，再找到每个物资批次的relatedId，加上相应的数目
        // 同时查找绑定当前批次的仓位，修改仓位状态为0-可使用
        List<GoodsBatch> gb = recordService.getGoods(2, orderId);
        for (GoodsBatch g : gb) {
            System.out.println(g);
            GoodsBatch wg = recordService.getGoodsBatchById(g.getRelatedId());
            wg.setNum(wg.getNum()+g.getNum());
            recordService.updateGoodsBatch(wg);
            System.out.println(wg.getId());
            List<Position> list = positionService.findPositionByGoods(g.getId());
            list.stream().map(l -> {
                l.setGoodsType(null);
                l.setState(0);
                l.setGoodsNum(0.0f);
                l.setManufactureDate(null);
                l.setExpirationDate(null);
                l.setBatchInfo(null);
                return l;
            }).collect(Collectors.toList());
            positionService.updatePositions(list);
        }
    }

    public void putIntoStagingArea(Long orderId) {
        List<GoodsBatch> gb = recordService.getGoods(2, orderId);
        for (GoodsBatch g : gb) {
            // 计算暂存区余位
            Goods goods = externalClient.findGoodsById(g.getGoodsId());
            List<Position> stagingList = positionService.findPositions(null,0, goods.getGoodsType(), 0);
            Float num = g.getNum();
            Float totalCapacity = 0.0f;
            for (Position s : stagingList) {
                totalCapacity += s.getCapacity();
            }
            System.out.println(totalCapacity);
            //仓库余位不足（如果入库单存在一批货物无法放入暂存区则该单所有货物都不能终止入库任务）
            if (totalCapacity < num) {
                throw new RuntimeException("暂存区仓库余位不足，无法执行当前任务，请联系管理员");
            }
        }
        for (GoodsBatch g : gb) {
            List<Position> list = positionService.findPositionByGoods(g.getId());
            for (Position l : list) {
                l.setGoodsType(null);
                l.setState(0);
                l.setGoodsNum(0.0f);
                l.setManufactureDate(null);
                l.setExpirationDate(null);
                l.setBatchInfo(null);
            }
            positionService.updatePositions(list);
            // 查找暂存区 当前类型 未占用货位并放进去
            Goods goods = externalClient.findGoodsById(g.getGoodsId());
            List<Position> stagingList = positionService.findPositions(null,0, goods.getGoodsType(), 0);
            List<Position> nStagingList = new ArrayList<>();
            Float num = g.getNum();
            g.setInDate(new Date());
            for (Position s : stagingList) {
                Float capacity = s.getCapacity();
                if(num<=0) break;
                s.setGoodsType(g.getGoodsId());
                s.setState(1);//仓位状态：已占用
                s.setGoodsNum(num > capacity ? capacity : num);
                s.setManufactureDate(g.getManufactureDate());
                s.setExpirationDate(g.getExpirationDate());
                s.setBatchInfo(g.getId());
                s.setInDate(g.getInDate());
                nStagingList.add(s);
                num -= capacity;
            }
            positionService.updatePositions(nStagingList);
        }
    }

    public void putIntoWareHouse(Long orderId) {
        List<GoodsBatch> gb = recordService.getGoods(2, orderId);
        for (GoodsBatch g : gb) {
            g.setInDate(new Date());
            List<Position> list = positionService.findPositionByGoods(g.getId());
            list.stream().map(l -> {
                l.setState(1);//货位状态：已占用
                l.setInDate(g.getInDate());//入库时间
                return l;
            }).collect(Collectors.toList());
            positionService.updatePositions(list);
            recordService.updateGoodsBatch(g);
        }
    }

    public List<InAndOut> statistics() {
        QueryWrapper q1 = new QueryWrapper();
        q1.eq("time",new java.sql.Date(new java.util.Date().getTime()));
        q1.eq("type",2);
        InAndOut i;
        if(inAndOutMapper.exists(q1)) {
            i = inAndOutMapper.selectOne(q1);
        } else {
            i = new InAndOut();
            i.setTime(new java.sql.Date(new java.util.Date().getTime()));
            i.setType(2);
            inAndOutMapper.insert(i);
        }
        i.setGoodsNum(0.0f);
        i.setGoodsPrice(0.0);
        QueryWrapper q2 = new QueryWrapper();
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN); // 当天开始时间
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX); // 当天结束时间
        q2.between("update_time", startOfDay, endOfDay);
        q2.eq("state",6);
        List<WarehousingRecord> w = warehousingMapper.selectList(q2);
        for(WarehousingRecord wr:w) {
            List<GoodsBatch> gbl = recordService.getGoods(2,wr.getId());
            for(GoodsBatch gb:gbl) {
                i.setGoodsNum(i.getGoodsNum()+gb.getNum());
            }
            i.setGoodsPrice(i.getGoodsPrice()+wr.getGoodsPrice());
        }
        inAndOutMapper.updateById(i);

        QueryWrapper q3 = new QueryWrapper();
        q3.eq("type",2);
        List<InAndOut> list = inAndOutMapper.selectList(q3);
        for(InAndOut a:list) {
            QueryWrapper q4 = new QueryWrapper();
            LocalDateTime dateTime = LocalDateTime.ofInstant(a.getTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime start = dateTime.with(LocalTime.MIN); // 当天开始时间
            LocalDateTime end = dateTime.with(LocalTime.MAX); // 当天结束时间
            q4.between("update_time", start, end);
            q4.eq("state",6);
            List<WarehousingRecord> r = warehousingMapper.selectList(q4);
            Map<Long, Goods> gMap = new HashMap<>();
            Map<Long, Supplier> sMap = new HashMap<>();
            for(WarehousingRecord wr:r) {
                List<GoodsBatch> gbl = recordService.getGoods(2,wr.getId());
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
                    Long supplierId = gb.getSupplierId();
                    Supplier supplier = sMap.get(supplierId);
                    if (supplier == null) {
                        supplier = externalClient.findSupplier(gb.getSupplierId());
                        supplier.setNum(gb.getNum());
                        supplier.setPrice(gb.getTotalPrice());
                        sMap.put(supplierId, supplier);
                    } else {
                        supplier.setNum(supplier.getNum()+gb.getNum());
                        supplier.setPrice(supplier.getPrice()+gb.getTotalPrice());
                    }
                }
            }
            List<Goods> gl = new ArrayList<>(gMap.values());
            List<Supplier> sl = new ArrayList<>(sMap.values());
            a.setGoods(gl);
            a.setSuppliers(sl);
        }
        return list;
    }

    public Long getInNum() {
        QueryWrapper q1 = new QueryWrapper();
        System.out.println(new java.sql.Date(new java.util.Date().getTime()));
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println("开始时间:" +startDateTime + " 结束时间:" + endDateTime);
        q1.eq("state",6);
        q1.between("update_time", startDateTime, endDateTime);
        return warehousingMapper.selectCount(q1);
    }
}