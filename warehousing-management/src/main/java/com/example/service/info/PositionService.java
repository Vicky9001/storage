package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.ExternalClient;
import com.example.entity.Info.Area;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.mapper.info.AreaMapper;
import com.example.mapper.info.PositionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PositionService {
    @Resource
    PositionMapper mapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    ExternalClient externalClient;

    public List<Position> getInfo(Long id,Integer state) {
        QueryWrapper q = new QueryWrapper();
        if(id!=null) q.eq("position_id",id);
        if(state!=null) q.eq("state", state);
        List<Position> list =  mapper.selectList(q);
        for (Position p: list) {
            Area a = areaMapper.get(p.getAreaId());
            p.setArea(a);
            if(p.getGoodsType() !=null){
                Goods g = externalClient.findGoodsById(p.getGoodsType());
                p.setGoods(g);
            }else p.setGoods(null);
        }
        return list;
    }

    public int addPosition(Long id, Long type){
        Position p = new Position();
        Long s_id = (mapper.getShelveId(id)!=null)?mapper.getShelveId(id)+1:1;
        p.setAreaId(id);
        p.setShelveId(s_id);
        if(type!=null)
            p.setGoodsType(type);
        return mapper.insert(p);
    }

    public int update(Position position){
        Position info = mapper.selectPosition(position.getAreaId(),position.getShelveId());
        position.setPositionId(info.getPositionId());
        return mapper.updateById(position);
    }

    public int delete(Position position) {
        Position info = mapper.selectPosition(position.getAreaId(),position.getShelveId());
        return mapper.deleteById(info.getPositionId());
    }

    public int check(Long area_id) {
        int flag = 1;
        Integer goods_num = mapper.selectGoodsNum(area_id);
        if(goods_num==null){
          return flag;
        }else if(goods_num<0){
            flag = -1;
        }
        return flag;
    }

    public int deleteArea(Long area_id) {
        return mapper.deleteByArea(area_id);
    }

    public void updatePositions(List<Position> list) {
        for(Position p:list) {
            System.out.println(p);
            mapper.updateById(p);
        }
    }

    public Position findPosition(Long id) {
        Position p = mapper.selectById(id);
        Area a = areaMapper.selectById(p.getAreaId());
        p.setArea(a);
        return p;
    }

    public List<Position> findPositionByGoods(Long id) {
        QueryWrapper w = new QueryWrapper();
        Map<String, Object> qm = new HashMap<>();
        qm.put("batch_info", id);
        w.allEq(qm);
        return mapper.selectList(w);
    }

    public List<Position> findPositions(Long areaId, Integer areaType, Integer goodsType, Integer state) {
        QueryWrapper<Position> wrapper = new QueryWrapper<>();
        if(areaId != null) wrapper.eq("area_id", areaId);
        if(areaType != null) wrapper.inSql("area_id", "SELECT id FROM area WHERE area_type = " + areaType);
        if(goodsType != null) wrapper.inSql("area_id", "SELECT id FROM area WHERE house_type = " + goodsType);
        if(state != null) wrapper.eq("state", state);
        return mapper.selectList(wrapper);
    }

    public List<Position> findOutPositions(Long goodsId, Integer areaType) {
        QueryWrapper<Position> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_type",goodsId);
        wrapper.eq("state",1);
        wrapper.inSql("area_id", "SELECT id FROM area WHERE area_type = " + areaType);
        wrapper.orderByAsc("expiration_date");
        return mapper.selectList(wrapper);
    }

    public List<Position> searchPositions(Long areaId, Integer areaType, Long goodsId, Long batchInfo, Integer state) {
        QueryWrapper<Position> wrapper = new QueryWrapper<>();
        if(areaId != null) wrapper.eq("area_id", areaId);
        if(areaType != null) wrapper.inSql("area_id", "SELECT id FROM area WHERE area_type = " + areaType);
        if(goodsId != null) wrapper.eq("goods_type",goodsId);
        if(batchInfo != null) wrapper.eq("batch_info",batchInfo);
        if(state != null) wrapper.eq("state", state);
        List<Position> l =  mapper.selectList(wrapper);
        l.stream().map(p -> {
            p.setGoods(externalClient.findGoodsById((p.getGoodsType())));
            return p;
        }).collect(Collectors.toList());
        return l;
    }
}
