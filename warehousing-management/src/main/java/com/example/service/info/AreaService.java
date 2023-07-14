package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Info.Area;
import com.example.mapper.info.AreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AreaService {
    @Resource
    AreaMapper mapper;
    public List<Area> getInfo(Long id, String name,Integer goodsType) {
        QueryWrapper wrapper = new QueryWrapper();
        Map<String, Object> qm = new HashMap<>();
        if(id!=null) qm.put("id", id);
        if(name!=null && name!="") qm.put("house_name", name);
        if(goodsType!=null) qm.put("house_type", goodsType);
        wrapper.allEq(qm);
        return mapper.selectList(wrapper);
    }

    public List<Area> getAreaByType(Long areaId, Integer areaType,Integer houseType) {
        QueryWrapper wrapper = new QueryWrapper();
        Map<String, Object> qm = new HashMap<>();
        if(areaId!=null) qm.put("id", areaId);
        if(areaType!=null) qm.put("area_type", areaType);
        if(houseType!=null) qm.put("house_type", houseType);
        wrapper.allEq(qm);
        return mapper.selectList(wrapper);
    }

    public List<String> getHouse() {
        List<String> houses =  mapper.getHouse();
        return houses;
    }

    public int addArea(String name,Integer housetype,List<Integer> type){
        Integer f = 1;
        for(Integer i:type){
            Area a = new Area(name,housetype,i);
            f = mapper.insert(a);
            if(f<0) return f;
        }
        return f;
    }

    public int delete(Area area) {
        return mapper.deleteById(area.getId());
    }

    public Long getId(Area a) {
        return  mapper.selectArea(a.getHouseName(),a.getAreaType()).getId();
    }

    public Area selectInfo(Long id) {
        return mapper.get(id);
    }
}
