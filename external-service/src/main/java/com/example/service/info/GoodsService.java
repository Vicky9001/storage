package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Info.Goods;
import com.example.entity.Info.GoodsSupplier;
import com.example.mapper.info.GoodsMapper;
import com.example.mapper.info.GoodsSupplierMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Resource
    private GoodsMapper mapper;
    @Resource
    private GoodsSupplierMapper goodsSupplierMapper;
    public List<Goods> getGoodsList(String name) {
        QueryWrapper<Goods> qw = new QueryWrapper<>();
        if(name!=null&&name!="") qw.like("goods_name",name);
        List<Goods> goods =  mapper.selectList(qw);
        return goods;
    }

    public int addGoods(Goods goods){
        return mapper.insert(goods);
    }

    public int update(Goods goods){
        Goods info = mapper.selectByName(goods.getGoodsName());
        goods.setGoodsId(info.getGoodsId());
        return mapper.updateById(goods);
    }

    public int delete(Goods customer) {
        Goods info = mapper.selectByName(customer.getGoodsName());
        return mapper.deleteById(info.getGoodsId());
    }

    public Goods getGoods(Long id) {
        return mapper.selectOne(new QueryWrapper<Goods>().eq("goods_id",id));
    }

    public List<Goods> get(Long supplierId) {
        List<Goods> goodsList = mapper.selectList(new QueryWrapper<Goods>().inSql("goods_id", "SELECT goods_id FROM goods_supplier WHERE supplier_id = " + supplierId));
        goodsList.stream().map(goods -> {
            QueryWrapper<GoodsSupplier> queryWrapper = new QueryWrapper<>();
            Map<String, Object> queryParamsMap = new HashMap<>();
            queryParamsMap.put("supplier_id", supplierId);
            queryParamsMap.put("goods_id", goods.getGoodsId());
            queryWrapper.allEq(queryParamsMap);
            GoodsSupplier info = goodsSupplierMapper.selectOne(queryWrapper);
            goods.setUnitPrice(info.getUnitPrice());
            goods.setUnitWeight(info.getUnitWeight());
            return goods;
        }).collect(Collectors.toList());
        return goodsList;
    }

    public GoodsSupplier getGoodsSupplier(Long supplierId, Long goodsId) {
        QueryWrapper<GoodsSupplier> queryWrapper = new QueryWrapper<>();
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("supplier_id", supplierId);
        queryParamsMap.put("goods_id", goodsId);
        queryWrapper.allEq(queryParamsMap);
        GoodsSupplier info = goodsSupplierMapper.selectOne(queryWrapper);
        return info;
    }
}
