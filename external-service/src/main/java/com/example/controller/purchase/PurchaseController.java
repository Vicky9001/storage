package com.example.controller.purchase;


import com.example.entity.Task.PurchaseRecord;
import com.example.service.purchase.PurchaseService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Resource
    PurchaseService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/purchase")
    public PurchaseRecord findOrder(@RequestParam(required = false, name = "id") Long id) {
        PurchaseRecord p = service.getPurchase(id);
        return p;
    }

    @GetMapping("/purchaseOrder")  // "token:xxx"
    public Result getOrder(@RequestParam(name = "type") List<Integer> type,
                           @RequestParam(required = false, name = "id") Long id) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<PurchaseRecord> list = service.getList(type, id);
            map.put("purchaseOrder", list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @PostMapping("/createPurchase")  // "token:xxx"
    public Result createOrder(@RequestBody PurchaseRecord purchaseRecord) {
        Result res = new Result();
        PurchaseRecord r = service.addPurchase(purchaseRecord);
        if (r != null) {
            res.setMessage("采购单创建成功");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }

        return res;
    }

    @PostMapping("/approve")  // "token:xxx"
    public Result approveOrder(@Validated @RequestBody  Map<String,Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        String remark = (String) params.get("remark");
        Long operator = Long.parseLong(params.get("operator").toString());
        Result res = new Result();
        PurchaseRecord r = service.approve(id, remark, operator);
        if (r != null) {
            res.setMessage("采购单审核通过");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.EXISTERR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @PostMapping("/reject")  // "token:xxx"
    public Result rejectOrder(@Validated @RequestBody  Map<String,Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        String remark = (String) params.get("remark");
        Long operator = Long.parseLong(params.get("operator").toString());
        Result res = new Result();
        PurchaseRecord r = service.reject(id,remark,operator);
        if (r != null) {
            res.setMessage("采购单审核未通过");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @PutMapping("/update")  // "token:xxx"
    public Result updateOrder(@RequestBody PurchaseRecord purchaseRecord) {
        Result res = new Result();
        PurchaseRecord r = service.update(purchaseRecord);
        if (r != null) {
            res.setMessage("采购单修改成功");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @PostMapping("/pay")  // "token:xxx"
    public Result pay(@Validated @RequestBody  Map<String,Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        Long operator = Long.parseLong(params.get("operator").toString());
        Result res = new Result();
        PurchaseRecord r = service.pay(id, operator);
        if (r != null) {
            res.setMessage("支付成功");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }


    @PostMapping("/reserve")  // "token:xxx"
    public Result reserve(@Validated @RequestBody  Map<String,Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        String remark = (String) params.get("remark");
        Long operator = Long.parseLong(params.get("operator").toString());
        Result res = new Result();
        PurchaseRecord r = service.reserve(id,remark, operator);
        if (r != null) {
            res.setMessage("采购单预约入库成功");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
    //获取近七天采购单数量
    @GetMapping("/purchaseNum")  // "token:xxx"
    public Result getNum() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long num = service.getPurchaseNum();
            map.put("purchaseNum", num);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/totalPrice")  // 获取仓库近一周流水
    public Result getOrderPrice(){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Double num = service.getOrderPrice();
            map.put("price", num);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }
}
