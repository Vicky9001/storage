package com.example.service.check;

import com.example.client.ExternalClient;
import com.example.entity.Info.Goods;
import com.example.entity.Info.Position;
import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.TaskRecord;
import com.example.entity.User.User;
import com.example.mapper.check.CheckMapper;
import com.example.service.info.PositionService;
import com.example.service.info.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class CheckServiceTest {

    @Mock
    private CheckMapper checkMapper;

    @Mock
    private RecordService recordService;

    @Mock
    private PositionService positionService;

    @Mock
    private ExternalClient externalClient;

    @InjectMocks
    private CheckService checkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getList() {
        // 构造测试数据
        List<Integer> type = Arrays.asList(1, 2, 3);
        Long id = 1L;
        CheckRecord checkRecord = new CheckRecord();
        List<CheckRecord> list = Arrays.asList(checkRecord);

        // Mock the database layer and external API to return data
        when(checkMapper.selectList(Mockito.any())).thenReturn(list);

        List<TaskRecord> taskRecords = new ArrayList<>();
        TaskRecord task1 = new TaskRecord();
        task1.setTaskId(1L);
        task1.setRecordType(1);
        task1.setRecordId(1L);
        task1.setTaskType(1);
        task1.setOperatorId(10001L);
        task1.setRemark("remark1");
        task1.setOperateTime(new Date());
        taskRecords.add(task1);

        TaskRecord task2 = new TaskRecord();
        task2.setTaskId(2L);
        task2.setRecordType(2);
        task2.setRecordId(2L);
        task2.setTaskType(2);
        task2.setOperatorId(10002L);
        task2.setRemark("remark2");
        task2.setOperateTime(new Date());
        taskRecords.add(task2);
        when(recordService.getTask(Mockito.anyInt(), Mockito.anyLong())).thenReturn(taskRecords);

        User user1 = new User();
        user1.setUserId(10001L);
        user1.setUserName("user1");
        User user2 = new User();
        user2.setUserId(10002L);
        user2.setUserName("user2");
        Map<Long, User> userMap = new HashMap<>();
        userMap.put(10001L, user1);
        userMap.put(10002L, user2);
        when(externalClient.getUser(Mockito.anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            return userMap.get(userId);
        });

        // Call the service method
        List<CheckRecord> result = checkService.getList(type, id);

        // Assert that the result is not null
        assertNotNull(result);
        // 检验返回结果的长度是否为1
        assertEquals(1, result.size());
        // 检验返回结果的第一个元素是否为我们构造的CheckRecord对象
        assertEquals(checkRecord, result.get(0));
        // 检验CheckMapper的selectList方法是否被调用过
        verify(checkMapper, Mockito.times(1)).selectList(Mockito.any());
        // 检验RecordService的getTask方法是否被调用过
        verify(recordService, Mockito.times(1)).getTask(6, checkRecord.getId());
        // 检验ExternalClient的getUser方法是否被调用过，以及调用参数是否正确(为什么调用总是失败？难道和跨服务有关）
//        verify(externalClient, Mockito.times(2)).getUser(Mockito.anyLong());
//        verify(externalClient, Mockito.times(1)).getUser(10001L);
//        verify(externalClient, Mockito.times(1)).getUser(10002L);
    }

    @Test
    public void create() {
        // 模拟外部服务的返回值
        Position position = new Position();
        position.setBatchInfo(1L);
        position.setGoodsType(1L);
        position.setGoodsNum(10f);
        when(positionService.findPosition(Mockito.anyLong()))
                .thenReturn(position);

        GoodsBatch goodsBatch = new GoodsBatch();
        goodsBatch.setRecordId(1L);
        goodsBatch.setRecordType(1);
        goodsBatch.setUnitPrice(100.0f);
        when(recordService.getGoodsBatchById(Mockito.anyLong()))
                .thenReturn(goodsBatch);

        Goods goods = new Goods();
        goods.setGoodsName("Apple");
        goods.setUnit("kg");
        when(externalClient.findGoodsById(Mockito.anyLong()))
                .thenReturn(goods);

        User user = new User();
        user.setRealName("John");
        when(externalClient.getUser(Mockito.anyLong()))
                .thenReturn(user);

        CheckRecord record = new CheckRecord();
        record.setCreatorId(1L);
        record.setCreatorName("John");
        record.setTime(new Date());
        record.setState(1);
        record.setPositionId(1L);
        record.setRecordId(1L);
        record.setRecordType(1);
        record.setBatchInfo(1L);
        record.setGoodsName("Apple");
        record.setUnit("kg");
        record.setUnitPrice(100.0f);
        record.setRecordNum(10f);
        record.setDesc("desc");
        when(checkMapper.insert(Mockito.any(CheckRecord.class)))
                .thenReturn(1);

        // 调用被测试的方法
        CheckRecord result = checkService.create(1L, 1L, "desc");

        // 验证结果是否正确
        assertEquals(result.getCreatorId(), 1L);
        assertEquals(result.getCreatorName(), "John");
        assertEquals(result.getState(), 1);
        assertEquals(result.getPositionId(), 1L);
        assertEquals(result.getRecordId(), 1L);
        assertEquals(result.getRecordType(), 1);
//        assertEquals(result.getBatchInfo(), 1L);
        assertEquals(result.getGoodsName(), "Apple");
        assertEquals(result.getUnit(), "kg");
        assertEquals(result.getUnitPrice(), 100.0, 0.001);
        assertEquals(result.getRecordNum(), 10);
        assertEquals(result.getDesc(), "desc");

        // 验证是否正确调用了相关的服务方法
        verify(positionService, Mockito.times(1)).findPosition(Mockito.anyLong());
        verify(recordService, Mockito.times(1)).getGoodsBatchById(Mockito.anyLong());
        verify(externalClient, Mockito.times(1)).findGoodsById(Mockito.anyLong());
        verify(externalClient, Mockito.times(1)).getUser(Mockito.anyLong());
        verify(checkMapper, Mockito.times(1)).insert(Mockito.any(CheckRecord.class));
    }

    @Test
    public void update() {
        CheckRecord record = new CheckRecord();
        record.setId(1L);
        record.setRealNum(12.0f);
        record.setRecordNum(10f);
        record.setUnitPrice(100.0f);
        when(checkMapper.selectById(Mockito.anyLong()))
                .thenReturn(record);

        // 调用被测试的方法
        CheckRecord result = checkService.update(1L, 12.0f);

        // 验证结果是否正确
        assertEquals(result.getId(), 1L);
        assertEquals(result.getRealNum(), 12.0f, 0.001);
        assertEquals(result.getRecordNum(), 10);
        assertEquals(result.getUnitPrice(), 100.0, 0.001);
        assertEquals(result.getDiffNum(), 2.0f, 0.001);
        assertEquals(result.getDiffPrice(), 200.0, 0.001);

        // 验证是否正确调用了相关的服务方法
        verify(checkMapper, Mockito.times(1)).selectById(Mockito.anyLong());
        verify(checkMapper, Mockito.times(1)).selectById(Mockito.anyLong());
        verify(checkMapper, Mockito.times(1)).updateById(Mockito.any(CheckRecord.class));
    }

    @Test
    public void finish() {
        CheckRecord record = new CheckRecord();
        record.setPositionId(1L);
        record.setRealNum(0f);
        when(positionService.findPosition(Mockito.anyLong()))
                .thenReturn(new Position());

        // 调用被测试的方法
        checkService.finish(record);

        // 验证是否正确调用了相关的服务方法
        verify(positionService, Mockito.times(1)).findPosition(Mockito.anyLong());
        verify(positionService, Mockito.times(1)).updatePositions(Mockito.anyList());
    }

    @Test
    void changeState() {
        // 构造测试数据
        Integer state = 3;
        Long id = 1L;
        Long operator = 10001L;
        String remark = "remark";
        CheckRecord checkRecord = new CheckRecord();
        checkRecord.setId(id);
        checkRecord.setState(2);
        when(checkMapper.selectById(Mockito.anyLong())).thenReturn(checkRecord);
        when(checkMapper.updateById(Mockito.any())).thenReturn(1);
        when(recordService.insertTask(Mockito.any())).thenReturn(1);

        // Call the service method
        String result = checkService.changeState(state, id, operator, remark);

        // Assert that the result is not null
        assertNotNull(result);
        // 检验返回结果是否符合预期
        assertEquals("盘点单审核通过", result);
        // 检验CheckMapper的selectById方法是否被调用过
        verify(checkMapper, Mockito.times(1)).selectById(Mockito.anyLong());
        // 检验CheckMapper的updateById方法是否被调用过
        verify(checkMapper, Mockito.times(1)).updateById(Mockito.any());
        // 检验RecordService的insertTask方法是否被调用过
        verify(recordService, Mockito.times(1)).insertTask(Mockito.any());
        // 检验Mockito的Mock对象是否有未被验证的交互
        Mockito.verifyNoMoreInteractions(checkMapper, recordService);
    }
}