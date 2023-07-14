package com.example.controller.transfer;


import com.example.entity.Task.TransferRecord;
import com.example.service.transfer.TransferService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Resource
    TransferService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/transferOrder")  // 获取移库单
    public Result getOrder(@RequestParam(name = "type") List<Integer> type,
                           @RequestParam(required = false, name = "id") Long id) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<TransferRecord> list = service.getList(type, id);
            map.put("transferOrder", list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @PostMapping("/create")  // "token:xxx"
    public Result createOrder(@Validated @RequestBody  Map<String,Object> params) {
        Long creatorId = Long.parseLong(params.get("creatorId").toString());
        Long originP = Long.parseLong(params.get("originPosition").toString());
        String desc = params.get("desc").toString();
        Integer targetArea = (Integer) params.get("targetArea");
        Float num = Float.valueOf((params.get("num").toString()));
        Result res = new Result();
        String r = service.create(creatorId,originP,targetArea,num,desc);
        if (r == "内配单创建成功") {
            res.setMessage("内配单创建成功");
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(r);
        }

        return res;
    }


    // 修改入库单状态并添加任务记录
    @PostMapping("/changeState")  // "token:xxx"
    public Result changeState(@Validated @RequestBody  Map<String,Object> params){
        Integer state = (Integer) params.get("state");
        Long id = Long.parseLong(params.get("id").toString());
        Long operator = Long.parseLong(params.get("operator").toString());
        String remark = (String) params.get("remark");
        Result res = new Result();
        String r = service.changeState(state, id, operator, remark);
        if (r != null) {
            res.setMessage(r);
        } else {
            res.setCode(ResultCode.EXISTERR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    //获取近七天内配单数量
    @GetMapping("/transferNum")  // "token:xxx"
    public Result getNum() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long num = service.getTransferNum();
            map.put("transferNum", num);
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
