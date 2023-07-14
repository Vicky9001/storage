package com.example.client;

import com.example.entity.Task.OutRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="warehousing-service",url="localhost:8102")
public interface WarehouseClient {

    // 创建待入库单
    @PostMapping("/stokeIn/createWaitingWarehousing")
    public Integer createWaitingOrder(@RequestParam(required = false,name = "id") Long id);

    @PostMapping("/stokeOut/create")  //调拨出库
    OutRecord createOrder(@RequestBody OutRecord outRecord);

    @GetMapping("/stokeOut/outPrice")  // 获取出库单近一周流水
    Double getOrderPrice();
}
