package com.example.service.stokeIn;

import com.example.client.ExternalClient;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Info.Supplier;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.TaskRecord;
import com.example.entity.Task.WaitingRecord;
import com.example.entity.Task.WarehousingRecord;
import com.example.mapper.stokeIn.WaitingRecordMapper;
import com.example.mapper.stokeIn.WarehousingMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WarehousingServiceTest {

    @Mock
    private WaitingRecordMapper waitingRecordMapper;
    @Mock
    private RecordService recordService;
    @Mock
    private ExternalClient externalClient;
    @Mock
    private PositionService positionService;
    @Mock
    private WarehousingMapper warehousingMapper;
    @InjectMocks
    private WarehousingService warehousingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWaitingOrder() {
        Long recordId = 1L;
        WaitingRecord waitingRecord = new WaitingRecord();
        when(waitingRecordMapper.insert(any(WaitingRecord.class))).thenReturn(1);
        when(recordService.getGoods(anyInt(), eq(recordId))).thenReturn(new ArrayList<>());

        Integer result = warehousingService.createWaitingOrder(recordId);

        assertEquals(1, result);
        verify(waitingRecordMapper).insert(any(WaitingRecord.class));
        verify(recordService).getGoods(eq(1), eq(recordId));
        verify(recordService, never()).insertGoods(any(GoodsBatch.class));
    }

    @Test
    void testCreateWaitingOrder_WithGoods() {
        Long recordId = 1L;
        WaitingRecord waitingRecord = new WaitingRecord();
        List<GoodsBatch> goodsBatches = new ArrayList<>();
        goodsBatches.add(new GoodsBatch());
        when(waitingRecordMapper.insert(any(WaitingRecord.class))).thenReturn(1);
        when(recordService.getGoods(anyInt(), eq(recordId))).thenReturn(goodsBatches);
        when(recordService.insertGoods(any(GoodsBatch.class))).thenReturn(1);

        Integer result = warehousingService.createWaitingOrder(recordId);

        assertEquals(1, result);
        verify(waitingRecordMapper).insert(any(WaitingRecord.class));
        verify(recordService).getGoods(eq(1), eq(recordId));
        verify(recordService, times(1)).insertGoods(any(GoodsBatch.class));
    }
    @Test
    public void testGetWaitingList() {
        // 创建假数据
        Long supplierId = 1L;
        Long goodsId = 2L;
        List<GoodsBatch> expectedList = new ArrayList<>();
        // 设置mock对象的行为
        when(recordService.findGoodsList(eq(5), eq(supplierId), eq(goodsId))).thenReturn(expectedList);

        // 调用被测试的方法
        List<GoodsBatch> resultList = warehousingService.getWaitingList(supplierId, goodsId);

        // 断言结果是否符合预期
        assertEquals(expectedList, resultList);

        // 验证mock对象的方法是否被调用
        verify(recordService, times(1)).findGoodsList(eq(5), eq(supplierId), eq(goodsId));
    }
    @Test
    public void testChangeState() {
        Long orderId = 1L;
        Integer state = 2;
        Long userId = 2L;
        String remark = "Deleting order";

        WarehousingRecord warehousingRecord = new WarehousingRecord();
        when(warehousingMapper.selectById(orderId)).thenReturn(warehousingRecord);

        warehousingService.changeState(orderId, state, userId, remark);

        verify(warehousingMapper, times(1)).updateById(any(WarehousingRecord.class));
        verify(recordService, times(1)).insertTask(any(TaskRecord.class));
        // Add additional verifications or assertions based on the expected behavior
    }


}
