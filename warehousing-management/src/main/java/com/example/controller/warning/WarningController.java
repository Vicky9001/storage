package com.example.controller.warning;

import com.example.entity.Info.Position;
import com.example.entity.Task.Warning;
import com.example.entity.Task.WarningRecord;
import com.example.service.warning.WarningService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/warning")
public class WarningController {

    @Resource
    WarningService service;

    ResultCode resultCode = new ResultCode();

    // 预警规则列表
    @GetMapping("/warning")
    public Result getList(@RequestParam(required = false, name = "type") Integer type,
                          @RequestParam(required = false, name = "goodsId") Long goodsId) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<Warning> list = service.getList(type, goodsId);
            map.put("warning", list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    // 创建预警规则
    @PostMapping("/create")
    public Result createOrder(@RequestBody Warning warning) {
        Result res = new Result();
        try {
            Warning r = service.create(warning);
            if (r != null) {
                res.setMessage("预警规则创建成功");
                Map<String, Object> map = new HashMap<>();
                map.put("warning", r);
                res.setData(map);
            } else {
                res.setCode(ResultCode.ERROR);
                res.setMessage(resultCode.getMsg(ResultCode.ERROR));
            }
        } catch (Exception e) {
            res.setCode(ResultCode.ERROR);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    // 删除预警规则
    @DeleteMapping("/delete")
    public Result delWarning(@RequestBody Warning warning) {
        Result res = new Result();
        try {
            service.delete(warning);
            res.setMessage("预警规则删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    // 预警记录
    @GetMapping("/warningRecord")
    public Result getRecordList(@RequestParam(required = false, name = "type") Integer type,
                                @RequestParam(required = false, name = "goodsId") Long goodsId,
                                @RequestParam(required = false, name = "state") Integer state,
                                @RequestParam(required = false, name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<WarningRecord> list = service.getRecordList(type, goodsId, state, date);
            map.put("warningRecord", list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }


    // 仓位可视化预警
    @GetMapping("/visualization")  // "token:xxx"
    public Result visualization(){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<Position> list = service.visualization();
            map.put("positionList",list);
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