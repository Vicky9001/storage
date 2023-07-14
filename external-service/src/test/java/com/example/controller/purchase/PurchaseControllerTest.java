package com.example.controller.purchase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import com.example.entity.Task.PurchaseRecord;
import com.example.service.purchase.*;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.mockito.Mockito.when;

//Mockito 4规则和runner在JUnit 5中不起作用，因此MockitoRule和JUnit runner不能使用。
@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTest {

    @InjectMocks
    private PurchaseController controller;

    @Mock
    private PurchaseService service;

    private ResultCode resultCode = new ResultCode();

    @Test
    public void testFindOrder() throws Exception {
        // 设置测试数据
        Long id = 1L;
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.getPurchase(id)).thenReturn(record);

        // 调用被测试方法
        PurchaseRecord result = controller.findOrder(id);

        // 验证结果
        assertThat(result, equalTo(record));
    }

    @Test
    public void testGetOrder() throws Exception {
        // 设置测试数据
        List<Integer> type = Arrays.asList(1, 2, 3);
        Long id = 123L;
        PurchaseRecord record = new PurchaseRecord();
        List<PurchaseRecord> list = Arrays.asList(record);

        // 设置 mock 行为
        when(service.getList(type, id)).thenReturn(list);

        // 调用被测试方法
        Result result = controller.getOrder(type, id);

        // 验证结果
        assertThat(result.getMessage(), equalTo("返回成功"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(list));
    }

    @Test
    public void testCreateOrder() throws Exception {
        // 设置测试数据
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.addPurchase(record)).thenReturn(record);

        // 调用被测试方法
        Result result = controller.createOrder(record);

        // 验证结果
        assertThat(result.getMessage(), equalTo("采购单创建成功"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(record));
    }

    @Test
    public void testApproveOrder() throws Exception {
        // 设置测试数据
        Long id = 123L;
        String remark = "test";
        Long operator = 456L;
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.approve(id, remark, operator)).thenReturn(record);

        // 调用被测试方法
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("remark", remark);
        params.put("operator", operator);
        Result result = controller.approveOrder(params);

        // 验证结果
        assertThat(result.getMessage(), equalTo("采购单审核通过"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(record));
    }

    @Test
    public void testRejectOrder() throws Exception {
        // 设置测试数据
        Long id = 123L;
        String remark = "test";
        Long operator = 456L;
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.reject(id, remark, operator)).thenReturn(record);

        // 调用被测试方法
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("remark", remark);
        params.put("operator", operator);
        Result result = controller.rejectOrder(params);

        // 验证结果
        assertThat(result.getMessage(), equalTo("采购单审核未通过"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(record));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        // 设置测试数据
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.update(record)).thenReturn(record);

        // 调用被测试方法
        Result result = controller.updateOrder(record);

        // 验证结果
        assertThat(result.getMessage(), equalTo("采购单修改成功"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(record));
    }

    @Test
    public void testReserve() throws Exception {
        // 设置测试数据
        Long id = 123L;
        String remark = "test";
        Long operator = 456L;
        PurchaseRecord record = new PurchaseRecord();

        // 设置 mock 行为
        when(service.reserve(id, remark, operator)).thenReturn(record);

        // 调用被测试方法
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("remark", remark);
        params.put("operator", operator);
        Result result = controller.reserve(params);

        // 验证结果
        assertThat(result.getMessage(), equalTo("采购单预约入库成功"));
        assertThat(result.getData().get("purchaseOrder"), equalTo(record));
    }

    @Test
    public void testGetNum() throws Exception {
        // 设置测试数据
        Long num = 123L;
        when(service.getPurchaseNum()).thenReturn(num);

        // 调用被测试方法
        Result result = controller.getNum();

        // 验证结果
        assertThat(result.getMessage(), equalTo("返回成功"));
        assertThat(result.getData().get("purchaseNum"), equalTo(num));
    }

    @Test
    public void testGetOrderPrice() throws Exception {
        // 设置测试数据
        Double price = 45.67;
        when(service.getOrderPrice()).thenReturn(price);

        // 调用被测试方法
        Result result = controller.getOrderPrice();

        // 验证结果
        assertThat(result.getMessage(), equalTo("返回成功"));
        assertThat(result.getData().get("price"), equalTo(price));
    }

}
