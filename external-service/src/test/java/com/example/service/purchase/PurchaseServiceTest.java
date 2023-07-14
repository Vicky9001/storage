package com.example.service.purchase;

import com.example.entity.Info.Goods;
import com.example.entity.Task.*;
import com.example.entity.User.User;
import com.example.mapper.purchase.PurchaseMapper;
import com.example.service.info.*;
import com.example.service.record.RecordService;
import com.example.service.sys.UserService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseMapper purchaseMapper;
    @Mock
    private GoodsService goodsService;
    @Mock
    private RecordService recordService;
    @Mock
    private UserService userService;
    @InjectMocks
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPurchase() {
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setDesc("testMapper");
        purchaseRecord.setId(1l);
        when(purchaseMapper.selectOne(Mockito.any())).thenReturn(purchaseRecord);

        GoodsBatch gb = new GoodsBatch();
        gb.setHouseName("test goodsBatch");
        gb.setRecordId(1L);
        gb.setRecordType(1);
        gb.setUnitPrice(100.0f);
        List<GoodsBatch> gbl = Arrays.asList(gb);
        when(recordService.getGoods(Mockito.anyInt(), Mockito.anyLong())).thenReturn(gbl);

        Goods g = new Goods();
        g.setGoodsName("test goods");
        when(goodsService.getGoods(Mockito.any())).thenReturn(g);

        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setRemark("return test record");
        List<TaskRecord> taskRecordList = Arrays.asList(taskRecord);
        when(recordService.getTask(Mockito.anyInt(), Mockito.any())).thenReturn(taskRecordList);

        User user = new User();
        user.setRealName("John");
        when(userService.getUser(Mockito.anyLong())).thenReturn(user);

        MockitoAnnotations.openMocks(this.purchaseService);
        PurchaseRecord result = purchaseService.getPurchase(1l);

        // Assert that the result is not null
        Assertions.assertNotNull(result);
        // 检验返回结果的第一个元素是否为我们构造的CheckRecord对象
        assertEquals(purchaseRecord, result);

        // 验证是否正确调用了相关的服务方法
        verify(purchaseMapper, Mockito.times(1)).selectOne(Mockito.any());
        verify(recordService, Mockito.times(1)).getTask(Mockito.anyInt(),Mockito.any());
        verify(recordService, Mockito.times(1)).getGoods(Mockito.anyInt(),Mockito.any());
        verify(goodsService, Mockito.times(1)).getGoods(Mockito.any());
        verify(userService, Mockito.times(1)).getUser(Mockito.any());
    }

    @Test
    void getList() {
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setDesc("testMapper");
        purchaseRecord.setId(1l);
        List<PurchaseRecord> list = Arrays.asList(purchaseRecord);
        when(purchaseMapper.selectList(Mockito.any())).thenReturn(list);

        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setRemark("return test record");
        List<TaskRecord> taskRecordList = Arrays.asList(taskRecord);
        when(recordService.getTask(Mockito.anyInt(), Mockito.any())).thenReturn(taskRecordList);

        GoodsBatch gb = new GoodsBatch();
        gb.setHouseName("test goodsBatch");
        gb.setRecordId(1L);
        gb.setRecordType(1);
        gb.setUnitPrice(100.0f);
        List<GoodsBatch> gbl = Arrays.asList(gb);
        when(recordService.getGoods(Mockito.anyInt(), Mockito.anyLong())).thenReturn(gbl);

        User user = new User();
        user.setRealName("John");
        when(userService.getUser(Mockito.anyLong())).thenReturn(user);

        Goods g = new Goods();
        g.setGoodsName("test goods");
        when(goodsService.getGoods(Mockito.any())).thenReturn(g);

        // Call the service method
        // 构造测试数据
        List<Integer> type = Arrays.asList(1);
        List<PurchaseRecord> result = purchaseService.getList(type,1l);

        // Assert that the result is not null
        Assertions.assertNotNull(result);
        // 检验返回结果的长度是否为1
        assertEquals(1, result.size());
        // 检验返回结果的第一个元素是否为我们构造的CheckRecord对象
        assertEquals(purchaseRecord, result.get(0));

        // 验证是否正确调用了相关的服务方法
        verify(purchaseMapper, Mockito.times(1)).selectList(Mockito.any());
        verify(recordService, Mockito.times(1)).getTask(Mockito.anyInt(),Mockito.any());
        verify(recordService, Mockito.times(1)).getGoods(Mockito.anyInt(),Mockito.any());
        verify(goodsService, Mockito.times(1)).getGoods(Mockito.any());
        verify(userService, Mockito.times(1)).getUser(Mockito.any());

    }
//    @Test
//    void create() {
//        when(outMapper.insert(Mockito.any())).thenReturn(1);
//        when(outMapper.updateById(Mockito.any())).thenReturn(1);
//
//        Position p = new Position();
//        p.setAreaId(0L);
//        p.setGoodsNum(3F);
//        p.setBatchInfo(1L);
//        List<Position> pl = Arrays.asList(p);
//        when(positionService.findOutPositions(Mockito.anyLong(),Mockito.anyInt())).thenReturn(pl);
//
//        GoodsBatch gb = new GoodsBatch();
//        gb.setHouseName("test goodsBatch");
//        gb.setGoodsId(1L);
//        gb.setManufactureDate(new Date());
//        gb.setExpirationDate(new Date());
//        gb.setUnitWeight(1F);
//        gb.setUnitPrice(1F);
//        gb.setSupplierId(1L);
//        gb.setId(1L);
//        gb.setPositionId(1L);
//        when(recordService.getGoodsBatchById(Mockito.anyLong())).thenReturn(gb);
//
//        when(recordService.insertGoods(Mockito.any())).thenReturn(1);
//        when(recordService.insertTask(Mockito.any())).thenReturn(1);
//        when(externalClient.findExpense(Mockito.anyInt())).thenReturn(1F);
//
//        OutRecord outRecord = new OutRecord();
//        outRecord.setGoodsId(1L);
//        outRecord.setNum(1F);
//        outRecord.setLogisticsType(2);
//        outRecord.setDesc("test");
//
//        OutRecord result = outService.create(outRecord);
//        // Assert that the result is not null
//        Assertions.assertNotNull(result);
//        assertEquals("test", result.getDesc());
//
//        // 验证是否正确调用了相关的服务方法
//        verify(outMapper, Mockito.times(1)).insert(Mockito.any());
//        verify(outMapper, Mockito.times(1)).updateById(Mockito.any());
//        verify(positionService, Mockito.times(1)).findOutPositions(Mockito.anyLong(),Mockito.anyInt());
//        verify(recordService, Mockito.times(1)).insertGoods(Mockito.any());
//        verify(recordService, Mockito.times(1)).insertTask(Mockito.any());
//        verify(externalClient, Mockito.times(1)).findExpense(Mockito.anyInt());
//
//
//    }
//    @Test
//    void changState() {
//        OutRecord outRecord = new OutRecord();
//        outRecord.setDesc("testMapper");
//        when(outMapper.selectById(Mockito.any())).thenReturn(outRecord);
//        when(outMapper.updateById(Mockito.any())).thenReturn(1);
//        when(recordService.insertTask(Mockito.any())).thenReturn(1);
//
//        String result = outService.changeState(16,1L,1L,"test");
//        assertEquals("出库拣货确认", result);
//
//        verify(outMapper, Mockito.times(1)).updateById(Mockito.any());
//        verify(outMapper, Mockito.times(1)).selectById(Mockito.any());
//        verify(recordService, Mockito.times(1)).insertTask(Mockito.any());
//
//
//    }
}
