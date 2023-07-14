package com.example.controller.outBound;

import com.example.client.WarehouseClient;
import com.example.entity.Task.OutRecord;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stokeOut")
public class OutBoundController {

    @Resource
    WarehouseClient warehouseClient;

    ResultCode resultCode = new ResultCode();
    @PostMapping("/create")  // "token:xxx"
    public Result createOrder(@RequestBody OutRecord outRecord) {
        Result res = new Result();
        OutRecord r = warehouseClient.createOrder(outRecord);
        if (r.getRemark()!=null) {
            res.setCode(ResultCode.ERROR);
            res.setMessage(r.getRemark());
        } else {
            res.setMessage("出库单创建成功");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("outOrder", r);
            res.setData(map);
        }
        return res;
    }

}
