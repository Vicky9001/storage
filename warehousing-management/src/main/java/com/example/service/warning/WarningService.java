package com.example.service.warning;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.Position;
import com.example.entity.Task.*;
import com.example.mapper.warning.WarningMapper;
import com.example.mapper.warning.WarningRecordMapper;
import com.example.service.info.PositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarningService {

    @Resource
    private WarningMapper warningMapper;
    @Resource
    private WarningRecordMapper warningRecordMapper;
    @Resource
    PositionService positionService;
    @Resource
    ExternalClient externalClient;

    public List<Warning> getList(Integer type, Long goodsId) {
        QueryWrapper wrapper = new QueryWrapper();
        if(type!=null){wrapper.eq("type",type);}
        if(goodsId!=null)wrapper.in("goods_id",goodsId);
        List<Warning> list = warningMapper.selectList(wrapper);
        for (Warning  p: list) {
            p.setGoods(externalClient.findGoodsById(p.getGoodsId()));
            p.setUser(externalClient.getUser(p.getCreatorId()));
        }
        return list;
    }

    // 创建预警规则
    public Warning create(Warning warning) {
        QueryWrapper q = new QueryWrapper();
        q.eq("goods_id", warning.getGoodsId());
        q.eq("type", warning.getType());
        if(warningMapper.exists(q)) throw new RuntimeException("已为该类型物资设置该类型规则，请先删除当前设置");
        warning.setTime(new Date());
        int f = warningMapper.insert(warning);
        return warning;
    }

    // 删除预警规则
    public Warning delete(Warning warning) {
        int f = warningMapper.deleteById(warning);
        if(f==0) return null;
        return warning;
    }

    public List<WarningRecord> getRecordList(Integer type, Long goodsId, Integer state, Date date) {
        QueryWrapper wrapper = new QueryWrapper();
        if(type!=null){wrapper.eq("type",type);}
        if(goodsId!=null)wrapper.eq("goods_id",goodsId);
        if(state!=null)wrapper.eq("state",state);
        if(date!=null){
            LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            LocalDateTime start = dateTime.with(LocalTime.MIN); // 当天开始时间
            LocalDateTime end = dateTime.with(LocalTime.MAX); // 当天结束时间
            wrapper.between("time", start, end);
        }
        List<WarningRecord> list = warningRecordMapper.selectList(wrapper);
        for (WarningRecord  p: list) {
            p.setGoods(externalClient.findGoodsById(p.getGoodsId()));
        }
        return list;
    }

    public List<Position> visualization() {
        List<Position> l = positionService.findPositions(null,null,null,null);
        l.stream().map(p ->{
            if(p.getGoodsType() != null) p.setGoods(externalClient.findGoodsById(p.getGoodsType()));
            if(p.getState()==1){
                QueryWrapper q = new QueryWrapper();
                q.eq("goods_id", p.getGoodsType());
                q.eq("state", 0);
                List<WarningRecord> r = warningRecordMapper.selectList(q);
                List<WarningRecord> nr = new ArrayList();
                for(WarningRecord record:r) {
                    if(record.getPositionId()==p.getPositionId()||record.getPositionId()==null) {
                        nr.add(record);
                    }
                }
                p.setWarningRecords(nr);
            }
            return p;
        }).collect(Collectors.toList());
        return l;
    }
}
