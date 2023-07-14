package com.example.controller.stokeOut;

import com.example.entity.Statistics.InAndOut;
import com.example.entity.Task.OutRecord;
import com.example.service.stokeOut.OutService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stokeOut")
public class OutController {

    @Resource
    OutService outService;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/outOrder")  // 获取出库单
    public Result getOrder(@RequestParam(name = "type") List<Integer> type) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<OutRecord> list = outService.getList(type);
            map.put("outOrder", list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/outPrice")  // 获取出库单近一周流水
    public Double getOrderPrice() {
        return outService.getPrice();
    }


    @PostMapping("/create")  //调拨出库
    public OutRecord createOrder(@RequestBody OutRecord outRecord) {
        OutRecord r = outService.create(outRecord);
        return r;
    }


    // 修改入库单状态并添加任务记录
    @PostMapping("/changeState")  // "token:xxx"
    public Result changeState(@Validated @RequestBody  Map<String,Object> params){
        Integer state = (Integer) params.get("state");
        Long id = Long.parseLong(params.get("id").toString());
        Long operator = Long.parseLong(params.get("operator").toString());
        String remark = (String) params.get("remark");
        Result res = new Result();
        String r = outService.changeState(state, id, operator, remark);
        if (r != null) {
            res.setMessage(r);
            if(r=="不予出库") res.setCode(ResultCode.EXISTERR);
        } else {
            res.setCode(ResultCode.EXISTERR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    // 获取出库统计数据
    @GetMapping("/statistics")  // "token:xxx"
    public Result statistics() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<InAndOut> list = outService.statistics();
            map.put("out",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }
    //获取近七天出库单数量
    @GetMapping("/outNum")  // "token:xxx"
    public Result getNum() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long num = outService.getOutNum();
            map.put("outNum", num);
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
