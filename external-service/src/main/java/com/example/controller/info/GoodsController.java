package com.example.controller.info;


import com.example.entity.Info.Goods;
import com.example.entity.Info.GoodsSupplier;
import com.example.service.info.GoodsService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    GoodsService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/findGoodsById")  // 通过id获取货物
    public Goods findGoodsById(@RequestParam(name = "id") Long id){
        Goods g = service.getGoods(id);
        return g;
    }

    @GetMapping("/findGoodsList")  //获取货物列表
    public List<Goods> findgoodsList(){
        List<Goods> list = service.getGoodsList(null);
        return list;
    }

    //获得供应商和货物提供的价钱和重量
    @GetMapping("/findInfo")  // 获取货物批次总价
    GoodsSupplier getInfo(@RequestParam(name = "supplierId") Long supplierId,
                          @RequestParam(name = "goodsId") Long goodsId){
        return service.getGoodsSupplier(supplierId,goodsId);
    }
    //返回该供应商提供的物资列表
    @GetMapping("/getGoods")
    public Result getSupplierGoods(@RequestParam(name = "supplierId") Long supplierId){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Goods> list = service.get(supplierId);
            map.put("supplierList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/goodsList")  // "token:xxx"
    public Result getGoodsList(@RequestParam(required = false,name = "name") String name){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Goods> list = service.getGoodsList(name);
            map.put("goodsList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }
    //  增加货物类型
    @PostMapping("/addGoods")
    public Result addGoods(@RequestBody Goods goods){
        Result res = new Result();
        try {
            int r = service.addGoods(goods);
            if(r > 0){
                res.setMessage("添加货物成功");
            }else{
                res.setCode(ResultCode.EXISTERR);
                res.setMessage(resultCode.getMsg(ResultCode.EXISTERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    // 更新供应商信息
    @PutMapping("/modGoods")
    public Result modGoods(@RequestBody Goods goods){
        Result res = new Result();
        try {
            int r = service.update(goods);
            if(r > 0){
                res.setMessage("信息更新成功");
            }else{
                res.setCode(ResultCode.InfoModERR);
                res.setMessage(resultCode.getMsg(ResultCode.InfoModERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
    // 删除供应商信息
    @DeleteMapping("/delGoods")
    public Result delGoods(@RequestBody List<Goods> goodsList){
        Result res = new Result();
        res.setMessage("信息删除成功");
        try {
            for (Goods g : goodsList) {
                int r = service.delete(g);
                if(r <= 0){
                    res.setCode(ResultCode.InfoModERR);
                    res.setMessage(resultCode.getMsg(ResultCode.InfoModERR));
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
}
