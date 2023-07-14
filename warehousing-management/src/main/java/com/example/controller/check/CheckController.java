package com.example.controller.check;


import com.example.entity.Task.CheckRecord;
import com.example.service.check.CheckService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/check")
public class CheckController {

    @Resource
    CheckService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/checkOrder")  // 获取盘点单
    public Result getOrder(@RequestParam(required = false, name = "type") List<Integer> type,
                           @RequestParam(required = false, name = "id") Long id) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<CheckRecord> list = service.getList(type, id);
            map.put("checkOrder", list);
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
        Long p = Long.parseLong(params.get("positionId").toString());
        String desc = params.get("desc").toString();
        Result res = new Result();
        CheckRecord r = service.create(creatorId,p,desc);
        if (r != null) {
            res.setMessage("盘点单创建成功");
            Map<String, Object> map = new HashMap<>();
            map.put("checkOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @PostMapping("/update")  // "token:xxx"
    public Result updateOrder(@Validated @RequestBody  Map<String,Object> params) {
        Long creatorId = Long.parseLong(params.get("orderId").toString());
        Float realNum = Float.valueOf(params.get("realNum").toString());
        Result res = new Result();
        CheckRecord r = service.update(creatorId,realNum);
        if (r != null) {
            res.setMessage("盘点数据更新成功");
            Map<String, Object> map = new HashMap<>();
            map.put("checkOrder", r);
            res.setData(map);
        } else {
            res.setCode(ResultCode.ERROR);
            res.setMessage("统计数量大于货架容量，请检查统计数据并重新录入");
        }
        return res;
    }


    // 修改盘点单状态并添加任务记录
    @PostMapping("/changeState")  // "token:xxx"
    public Result changeState(@Validated @RequestBody  Map<String,Object> params){
        Integer state = (Integer) params.get("state");
        Long id = Long.parseLong(params.get("orderId").toString());
        Long operator = Long.parseLong(params.get("userId").toString());
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
}
