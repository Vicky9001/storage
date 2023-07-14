package com.example.client;

import com.example.entity.Info.Department;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Logistics;
import com.example.entity.Info.Supplier;
import com.example.entity.User.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="external-service",url="localhost:8101")
public interface ExternalClient {
    //路径保证和其他微服务提供的一致即可
    @GetMapping("/user/getUser")  // 通过id找用户
    User getUser(@RequestParam(name = "id")Long id);

    @GetMapping("/goods/findGoodsById")  // 通过id获取货物
    Goods findGoodsById(@RequestParam(name = "id") Long id);

    @GetMapping("/goods/findGoodsList")  //获取货物列表
    List<Goods> findgoodsList();

    @GetMapping("/supplier/findSupplier")  // 根据id找供应商
    public Supplier findSupplier(@RequestParam(required = false,name = "id") Long id);
    @GetMapping("/logistics/findExpense")
    Float findExpense(@RequestParam(name = "id") Integer id);

    @GetMapping("/logistics/findLogistics")
    Logistics findLogistics(@RequestParam(name = "id") Integer id);

    @GetMapping("/department/findDepartment")
    Department findDepartment(@RequestParam(name = "departmentId") Long id);
}
