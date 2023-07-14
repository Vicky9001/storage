package com.example.controller.info;


import com.example.entity.Info.Supplier;
import com.example.service.info.SupplierService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    SupplierService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/findSupplier")  // 根据id找供应商
    public Supplier findSupplier(@RequestParam(required = false,name = "id") Long id){
        return service.findSupplier(id);
    }

    @GetMapping("/supplierList")  // "token:xxx"
    public Result getSupplierList(@RequestParam(required = false,name = "name") String name){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Supplier> list = service.getInfo(name);
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

    //返回提供该物资的供应商
    @GetMapping("/getSupplier")  // "token:xxx"
    public Result getSupplier(@RequestParam(name = "goodsId") Integer goodsId){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Supplier> list = service.get(goodsId);
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

    //  增加供应商
    @PostMapping("/addSupplier")
    public Result addSupplier(@RequestBody Supplier supplier){
        Result res = new Result();
        try {
            int r = service.addSupplier(supplier);
            if(r > 0){
                res.setMessage("添加供应商成功");
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
    @PutMapping("/modSupplier")
    public Result modSupplier(@RequestBody Supplier supplier){
        Result res = new Result();
        try {
            int r = service.update(supplier);
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
    @DeleteMapping("/delSupplier")
    public Result delSupplier(@RequestBody List<Supplier> supplierList){
        Result res = new Result();
        res.setMessage("信息删除成功");
        try {
            for (Supplier s : supplierList) {
                int r = service.delete(s);
                if(r <= 0){
                    res.setCode(ResultCode.InfoDelERR);
                    res.setMessage(resultCode.getMsg(ResultCode.InfoDelERR));
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
