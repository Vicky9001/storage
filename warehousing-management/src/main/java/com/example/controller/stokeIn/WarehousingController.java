package com.example.controller.stokeIn;

import com.example.entity.Info.Area;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Supplier;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.WarehousingRecord;
import com.example.entity.Statistics.InAndOut;
import com.example.service.stokeIn.WarehousingService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stokeIn")
public class WarehousingController {

    @Resource
    WarehousingService warehousingService;

    ResultCode resultCode = new ResultCode();

    // 创建待入库单
    @PostMapping("/createWaitingWarehousing")
    public Integer createWaitingOrder(@RequestParam(required = false,name = "id") Long id) {
        Result res = new Result();
        Integer r = null;
        try {
            r = warehousingService.createWaitingOrder(id);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage("操作失败：" + e.getMessage());
        }
        return r;
    }

    // 获取等待入库的每一批物资,可通过供应商、货物id查找记录
    @GetMapping("/waitingList")
    public Result getWaitingList(@RequestParam(required = false,name = "supplierId") Long supplierId,
                                   @RequestParam(required = false,name = "goodsId") Long goodsId) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<GoodsBatch> list = warehousingService.getWaitingList(supplierId,goodsId);
            map.put("goodsBatches",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    // 用于按照仓库查找 入库默认为1-普通存储区
    @GetMapping("/areaList")  // "token:xxx"
    public Result getAreaList(@RequestParam ("goodsId") Long goodsId) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Area> list = warehousingService.findAreaList(goodsId);
            map.put("area",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    // 为多批物资创建入库单
    @PostMapping("/createWarehousing")
    public Result createOrder(@RequestBody WarehousingRecord warehousingRecord) {
        Result res = new Result();
        try {
            String r = warehousingService.addWarehousing(warehousingRecord);
            res.setMessage(r);
        } catch (IllegalArgumentException e) {
            res.setCode(ResultCode.INVALID_PARAMETER);
            res.setMessage("操作失败：" + e.getMessage());
        } catch (RuntimeException e) {
            res.setCode(ResultCode.SERVER_ERROR);
            res.setMessage("操作失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage("操作失败：" + e.getMessage());
        }
        return res;
    }


    // 获取入库单
    @GetMapping("/warehousingOrder")
    public Result getOrderList(@RequestParam(required = false,name = "state") List<Integer> state,
                               @RequestParam(required = false,name = "id") Long id,
                           @RequestParam(required = false,name = "goodsId") Long goodsId,
                           @RequestParam(required = false,name = "supplier") Long supplier) {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<WarehousingRecord> list = warehousingService.getOrderList(state, id, goodsId, supplier);
            map.put("WarehousingOrder",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    // 修改入库单状态并添加任务记录
    @PostMapping("/changeState")  // "token:xxx"
    public Result changeState(@Validated @RequestBody  Map<String,Object> params){
        Long orderId = Long.parseLong(params.get("orderId").toString());
        Integer state = (Integer) params.get("state");//入库单下一个状态
        Long userId = Long.parseLong(params.get("userId").toString());
        String remark = (String) params.get("remark");
        System.out.println(state);
        Result res = new Result();
        try {
            warehousingService.changeState(orderId, state, userId, remark);
            res.setMessage("入库单状态修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage("操作失败：" + e.getMessage());
        }
        return res;
    }

    // 获取入库统计数据
    @GetMapping("/statistics")  // "token:xxx"
    public Result statistics() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<InAndOut> list = warehousingService.statistics();
            map.put("warehousing",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    //获取近七天采购单数量
    @GetMapping("/inNum")  // "token:xxx"
    public Result getNum() {
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long num = warehousingService.getInNum();
            map.put("warehousingNum", num);
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
