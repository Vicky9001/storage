package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Info.Distance;
import com.example.entity.Info.Logistics;
import com.example.entity.Info.Supplier;
import com.example.mapper.info.DistanceMapper;
import com.example.mapper.info.LogisticsMapper;
import com.example.mapper.info.SupplierMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogisticsService {
    @Resource
    private LogisticsMapper logisticsMapper;
    @Resource
    private DistanceMapper distanceMapper;
    @Resource
    private SupplierMapper supplierMapper;
    public List<Logistics> getLogisticsExpense() {
        return logisticsMapper.selectList(null);
    }

    public List<Distance> getDistance(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("related_type", 0);
        if(name!=null && name!="")
            wrapper.inSql("related_id", "SELECT supplier_id FROM supplier WHERE supplier_name like '%"+name+"%'");
        List<Distance> lst =  distanceMapper.selectList(wrapper);
        lst = lst.stream().map(l->{
            Supplier s = supplierMapper.selectById(l.getRelatedId());
            l.setS(s);
            return l;
        }).collect(Collectors.toList());
        return lst;
    }
    public Float findDistance(Integer relatedType, Long relatedId) {
        QueryWrapper wrapper = new QueryWrapper();
        Map<String,Object> map = new HashMap<>();
        map.put("related_type",relatedType);
        map.put("related_id",relatedId);
        wrapper.allEq(map);
        Distance d = distanceMapper.selectOne(wrapper);
        return d.getDistance();
    }

    public Float getExpense(Integer id) {
        return logisticsMapper.selectById(id).getUnitPrice();
    }

    public Logistics getLogistics(Integer id) {
        return logisticsMapper.selectById(id);
    }
}
