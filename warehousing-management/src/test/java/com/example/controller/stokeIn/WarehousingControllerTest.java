package com.example.controller.stokeIn;

import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.WarehousingRecord;
import com.example.service.stokeIn.WarehousingService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class WarehousingControllerTest {

    @Mock
    private WarehousingService warehousingService;

    @InjectMocks
    private WarehousingController warehousingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWaitingOrder() {
        Long id = 1L;
        Integer expectedResult = 1;

        when(warehousingService.createWaitingOrder(id)).thenReturn(expectedResult);

        Integer result = warehousingController.createWaitingOrder(id);

        assertEquals(expectedResult, result);
        verify(warehousingService, times(1)).createWaitingOrder(id);
    }
    @Test
    void testCreateWaitingOrder_Exception() {
        Long id = 1L;
        Exception exception = new RuntimeException("Test Exception");

        when(warehousingService.createWaitingOrder(id)).thenThrow(exception);

        Integer result = warehousingController.createWaitingOrder(id);
        verify(warehousingService, times(1)).createWaitingOrder(id);
    }
    @Test
    void testGetWaitingList() {
        Long supplierId = 1L;
        Long goodsId = 2L;
        List<GoodsBatch> expectedList = Arrays.asList(new GoodsBatch(), new GoodsBatch());

        when(warehousingService.getWaitingList(supplierId, goodsId)).thenReturn(expectedList);

        Result result = warehousingController.getWaitingList(supplierId, goodsId);

        assertEquals("返回成功", result.getMessage());
        assertEquals(expectedList, result.getData().get("goodsBatches"));
        verify(warehousingService, times(1)).getWaitingList(supplierId, goodsId);
    }
    @Test
    void testGetWaitingList_Exception() {
        Long supplierId = 1L;
        Long goodsId = 2L;
        Exception exception = new RuntimeException("Test Exception");

        when(warehousingService.getWaitingList(supplierId, goodsId)).thenThrow(exception);

        Result result = warehousingController.getWaitingList(supplierId, goodsId);

        assertEquals(ResultCode.InfoGetERR, result.getCode());
        verify(warehousingService, times(1)).getWaitingList(supplierId, goodsId);
    }
    @Test
    void testGetOrderList() {
        List<Integer> state = Arrays.asList(1, 2);
        Long id = 1L;
        Long goodsId = 2L;
        Long supplier = 3L;
        List<WarehousingRecord> expectedList = Arrays.asList(new WarehousingRecord(), new WarehousingRecord());

        when(warehousingService.getOrderList(state, id, goodsId, supplier)).thenReturn(expectedList);

        Result result = warehousingController.getOrderList(state, id, goodsId, supplier);

        assertEquals("返回成功", result.getMessage());
        assertEquals(expectedList, result.getData().get("WarehousingOrder"));
        verify(warehousingService, times(1)).getOrderList(state, id, goodsId, supplier);
    }
    @Test
    void testGetOrderList_Exception() {
        List<Integer> state = Arrays.asList(1, 2);
        Long id = 1L;
        Long goodsId = 2L;
        Long supplier = 3L;
        Exception exception = new RuntimeException("Test Exception");

        when(warehousingService.getOrderList(state, id, goodsId, supplier)).thenThrow(exception);

        Result result = warehousingController.getOrderList(state, id, goodsId, supplier);

        assertEquals(ResultCode.InfoGetERR, result.getCode());
        verify(warehousingService, times(1)).getOrderList(state, id, goodsId, supplier);
    }

    @Test
    void testChangeState() {
        Long orderId = 1L;
        Integer state = 2;
        Long userId = 3L;
        String remark = "Test";
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("state", state);
        params.put("userId", userId);
        params.put("remark", remark);

        Result result = new Result();

        doNothing().when(warehousingService).changeState(orderId, state, userId, remark);

        Result res = warehousingController.changeState(params);

        assertEquals("入库单状态修改成功", res.getMessage());
        verify(warehousingService, times(1)).changeState(orderId, state, userId, remark);
    }
    @Test
    void testChangeState_Exception() {
        Long orderId = 1L;
        Integer state = 2;
        Long userId = 3L;
        String remark = "Test remark";
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("state", state);
        params.put("userId", userId);
        params.put("remark", remark);
        Exception exception = new RuntimeException("Test Exception");

        doThrow(exception).when(warehousingService).changeState(orderId, state, userId, remark);

        Result result = warehousingController.changeState(params);

        assertEquals(ResultCode.ERROR, result.getCode());
        verify(warehousingService, times(1)).changeState(orderId, state, userId, remark);
    }

}
