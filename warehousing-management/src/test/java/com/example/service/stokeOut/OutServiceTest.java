package com.example.service.stokeOut;

import com.example.client.ExternalClient;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.OutRecord;
import com.example.entity.Task.TaskRecord;
import com.example.entity.User.User;
import com.example.mapper.check.CheckMapper;
import com.example.mapper.info.InAndOutMapper;
import com.example.mapper.stokeOut.OutMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import io.cucumber.java.ja.但し;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class OutServiceTest {

    @Mock
    private OutMapper outMapper;
    @Mock
    private InAndOutMapper inAndOutMapper;
    @Mock
    private RecordService recordService;
    @Mock
    private PositionService positionService;
    @Mock
    private ExternalClient externalClient;

    @InjectMocks
    private OutService outService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getList() {
        OutRecord outRecord = new OutRecord();
        outRecord.setDesc("testMapper");
        List<OutRecord> list = Arrays.asList(outRecord);
        when(outMapper.selectList(Mockito.any())).thenReturn(list);

        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setRemark("return test record");
        List<TaskRecord> taskRecordList = Arrays.asList(taskRecord);
        when(recordService.getTask(Mockito.anyInt(), Mockito.any())).thenReturn(taskRecordList);

        GoodsBatch gb = new GoodsBatch();
        gb.setHouseName("test goodsBatch");
        List<GoodsBatch> gbl = Arrays.asList(gb);
        when(recordService.getGoods(Mockito.anyInt(), Mockito.any())).thenReturn(gbl);

        User user = new User();
        user.setRealName("John");
        when(externalClient.getUser(Mockito.anyLong())).thenReturn(user);

        Goods g = new Goods();
        g.setGoodsName("test goods");
        when(externalClient.findGoodsById(Mockito.any())).thenReturn(g);

        // Call the service method
        // 构造测试数据
        List<Integer> type = Arrays.asList(1);
        List<OutRecord> result = outService.getList(type);

        // Assert that the result is not null
        assertNotNull(result);
        // 检验返回结果的长度是否为1
        assertEquals(1, result.size());
        // 检验返回结果的第一个元素是否为我们构造的CheckRecord对象
        assertEquals(outRecord, result.get(0));

        // 验证是否正确调用了相关的服务方法
        verify(outMapper, Mockito.times(1)).selectList(Mockito.any());
        verify(recordService, Mockito.times(1)).getTask(Mockito.anyInt(),Mockito.any());
        verify(recordService, Mockito.times(1)).getGoods(Mockito.anyInt(),Mockito.any());
        verify(externalClient, Mockito.times(1)).findGoodsById(Mockito.any());
        verify(externalClient, Mockito.times(1)).getUser(Mockito.any());

    }
    @Test
    void create() {
        when(outMapper.insert(Mockito.any())).thenReturn(1);
        when(outMapper.updateById(Mockito.any())).thenReturn(1);

        Position p = new Position();
        p.setAreaId(0L);
        p.setGoodsNum(3F);
        p.setBatchInfo(1L);
        List<Position> pl = Arrays.asList(p);
        when(positionService.findOutPositions(Mockito.anyLong(),Mockito.anyInt())).thenReturn(pl);

        GoodsBatch gb = new GoodsBatch();
        gb.setHouseName("test goodsBatch");
        gb.setGoodsId(1L);
        gb.setManufactureDate(new Date());
        gb.setExpirationDate(new Date());
        gb.setUnitWeight(1F);
        gb.setUnitPrice(1F);
        gb.setSupplierId(1L);
        gb.setId(1L);
        gb.setPositionId(1L);
        when(recordService.getGoodsBatchById(Mockito.anyLong())).thenReturn(gb);

        when(recordService.insertGoods(Mockito.any())).thenReturn(1);
        when(recordService.insertTask(Mockito.any())).thenReturn(1);
        when(externalClient.findExpense(Mockito.anyInt())).thenReturn(1F);

        OutRecord outRecord = new OutRecord();
        outRecord.setGoodsId(1L);
        outRecord.setNum(1F);
        outRecord.setLogisticsType(2);
        outRecord.setDesc("test");

        OutRecord result = outService.create(outRecord);
        // Assert that the result is not null
        assertNotNull(result);
        assertEquals("test", result.getDesc());

        // 验证是否正确调用了相关的服务方法
        verify(outMapper, Mockito.times(1)).insert(Mockito.any());
        verify(outMapper, Mockito.times(1)).updateById(Mockito.any());
        verify(positionService, Mockito.times(1)).findOutPositions(Mockito.anyLong(),Mockito.anyInt());
        verify(recordService, Mockito.times(1)).insertGoods(Mockito.any());
        verify(recordService, Mockito.times(1)).insertTask(Mockito.any());
        verify(externalClient, Mockito.times(1)).findExpense(Mockito.anyInt());


    }
    @Test
    void changState() {
        OutRecord outRecord = new OutRecord();
        outRecord.setDesc("testMapper");
        when(outMapper.selectById(Mockito.any())).thenReturn(outRecord);
        when(outMapper.updateById(Mockito.any())).thenReturn(1);
        when(recordService.insertTask(Mockito.any())).thenReturn(1);

        String result = outService.changeState(16,1L,1L,"test");
        assertEquals("出库拣货确认", result);

        verify(outMapper, Mockito.times(1)).updateById(Mockito.any());
        verify(outMapper, Mockito.times(1)).selectById(Mockito.any());
        verify(recordService, Mockito.times(1)).insertTask(Mockito.any());


    }
}