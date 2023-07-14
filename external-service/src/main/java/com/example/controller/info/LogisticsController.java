package com.example.controller.info;


import com.example.entity.Info.Distance;
import com.example.entity.Info.Logistics;
import com.example.service.info.LogisticsService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/logistics")
public class LogisticsController {

    @Resource
    LogisticsService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/logisticsList")  // "token:xxx"
    public Result getLogisticsExpense(){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Logistics> list = service.getLogisticsExpense();
            map.put("logisticsList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/distanceList")  // "token:xxx"
    public Result getDistance(@RequestParam(required = false,name = "name") String name){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Distance> list = service.getDistance(name);
            map.put("distanceList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/findDistance")  // "token:xxx"
    public Float findDistance(@RequestParam(name = "relatedType") Integer relatedType,
                              @RequestParam(name = "relatedId") Long relatedId){
        Float distance = service.findDistance(relatedType,relatedId);
        return distance;
    }

    @GetMapping("/findExpense")
    public Float findExpense(@RequestParam(name = "id") Integer id){
        Float price = service.getExpense(id);
        System.out.println(price);
        return price;
    }

    @GetMapping("/findLogistics")
    Logistics findLogistics(@RequestParam(name = "id") Integer id) {
        return service.getLogistics(id);
    }
}
