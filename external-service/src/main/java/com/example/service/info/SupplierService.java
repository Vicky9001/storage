package com.example.service.info;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Info.Supplier;
import com.example.mapper.info.SupplierMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SupplierService {
    @Resource
    private SupplierMapper mapper;
    public List<Supplier> getInfo(String name) {
        List<Supplier> suppliers =  mapper.getSupplier(name);
        return suppliers;
    }

    public int addSupplier(Supplier supplier){
        Supplier info = mapper.selectByName(supplier.getSupplierName());
        if(info==null){
            return mapper.insert(supplier);
        }else return -1;
    }
    public int update(Supplier supplier){
        return mapper.updateById(supplier);
    }

    public int delete(Supplier supplier) {
        return mapper.deleteById(supplier.getSupplierId());
    }

    public List<Supplier> get(Integer goodsId) {
        List<Supplier> supplierList = mapper.selectList(new QueryWrapper<Supplier>().inSql("supplier_id", "SELECT supplier_id FROM goods_supplier WHERE goods_id = " + goodsId));
        return supplierList;
    }

    public Supplier findSupplier(Long id) {
        QueryWrapper qw = new QueryWrapper();
        if(id!=null) qw.eq("supplier_id",id);
        return mapper.selectOne(qw);
    }
}
