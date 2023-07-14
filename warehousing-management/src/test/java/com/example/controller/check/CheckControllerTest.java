package com.example.controller.check;

import com.example.entity.Task.CheckRecord;
import com.example.service.check.CheckService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
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

@RunWith(MockitoJUnitRunner.class)
class CheckControllerTest {

    @Mock
    private CheckService checkService;

    @InjectMocks
    private CheckController checkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrder() {
        // Prepare test data
        List<Integer> type = Arrays.asList(1, 2, 3);
        Long id = 123L;
        List<CheckRecord> checkRecords = new ArrayList<>();
        checkRecords.add(new CheckRecord(1L, 123L, "creator1", new Date(), 1, "desc1", 1L, 1L, 1, 1L, "goods1", "unit1", 1.0f, 10.0f, 5.0f, 5.0f, 1.0f, null));
        checkRecords.add(new CheckRecord(2L, 123L, "creator2", new Date(), 1, "desc2", 2L, 2L, 2, 2L, "goods2", "unit2", 2.0f, 20.0f, 10.0f, 10.0f, 2.0f, null));

        // Mock the service layer to return data
        Mockito.when(checkService.getList(type, id)).thenReturn(checkRecords);

        // Call the controller endpoint
        Result result = checkController.getOrder(type, id);

        // Verify the results
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertEquals(result.getMessage(), "返回成功");
        assertNotNull(result.getData());

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertNotNull(data.get("checkOrder"));

        List<CheckRecord> resultRecords = (List<CheckRecord>) (data.get("checkOrder"));
        assertEquals(resultRecords.size(), 2);

        CheckRecord record1 = resultRecords.get(0);
        CheckRecord record2 = resultRecords.get(1);

        assertEquals(Long.valueOf(1L), record1.getId());
        assertEquals(Long.valueOf(123L), record1.getCreatorId());
        assertEquals("creator1", record1.getCreatorName());
        // assertEquals(new Date(), record1.getTime());
        assertEquals(Integer.valueOf(1), record1.getState());
        assertEquals("desc1", record1.getDesc());
        assertEquals(Long.valueOf(1L), record1.getPositionId());
        assertEquals(Long.valueOf(1L), record1.getRecordId());
        assertEquals(Integer.valueOf(1), record1.getRecordType());
        assertEquals(Long.valueOf(1L), record1.getBatchInfo());
        assertEquals("goods1", record1.getGoodsName());
        assertEquals("unit1", record1.getUnit());
        assertEquals(Float.valueOf(1.0f), record1.getUnitPrice());
        assertEquals(Float.valueOf(10.0f), record1.getRecordNum());
        assertEquals(Float.valueOf(5.0f), record1.getRealNum());
        assertEquals(Float.valueOf(5.0f), record1.getDiffNum());
        assertEquals(Float.valueOf(1.0f), record1.getDiffPrice());
        assertNull(record1.getTask());

        assertEquals(Long.valueOf(2L), record2.getId());
        assertEquals(Long.valueOf(123L), record2.getCreatorId());
        assertEquals("creator2", record2.getCreatorName());
        // assertEquals(new Date(), record2.getTime());
        assertEquals(Integer.valueOf(1), record2.getState());
        assertEquals("desc2", record2.getDesc());
        assertEquals(Long.valueOf(2L), record2.getPositionId());
        assertEquals(Long.valueOf(2L), record2.getRecordId());
        assertEquals(Integer.valueOf(2), record2.getRecordType());
        assertEquals(Long.valueOf(2L), record2.getBatchInfo());
        assertEquals("goods2", record2.getGoodsName());
        assertEquals("unit2", record2.getUnit());
        assertEquals(Float.valueOf(2.0f), record2.getUnitPrice());
        assertEquals(Float.valueOf(20.0f), record2.getRecordNum());
        assertEquals(Float.valueOf(10.0f), record2.getRealNum());
        assertEquals(Float.valueOf(10.0f), record2.getDiffNum());
        assertEquals(Float.valueOf(2.0f), record2.getDiffPrice());
        assertNull(record2.getTask());
    }

    @Test
    void createOrder() {
        // 构造测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("creatorId", 1L);
        params.put("positionId", 2L);
        params.put("desc", "test desc");
        CheckRecord checkRecord = new CheckRecord();

        // Mock the service layer to return data
        Mockito.when(checkService.create(1L, 2L, "test desc")).thenReturn(checkRecord);

        // Call the controller endpoint
        Result result = checkController.createOrder(params);

        // Assert that the result message is "盘点单创建成功"
        assertEquals(result.getMessage(), "盘点单创建成功");
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertNotNull(result.getData());

        // Assert that the result data contains the checkOrder object
        assertTrue(result.getData().containsKey("checkOrder"));
        assertEquals(checkRecord, result.getData().get("checkOrder"));
    }

    @Test
    void updateOrder() {
        // 构造测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", 1L);
        params.put("realNum", 10.5f);
        CheckRecord checkRecord = new CheckRecord();

        // Mock the service layer to return data
        Mockito.when(checkService.update(1L, 10.5f)).thenReturn(checkRecord);

        // Call the controller endpoint
        Result result = checkController.updateOrder(params);

        // Assert that the result is not null
        assertNotNull(result);

        // Assert that the result message is "盘点数据更新成功"
        assertEquals(result.getMessage(),"盘点数据更新成功" );
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertNotNull(result.getData());

        // Assert that the result data contains the checkOrder object
        assertTrue(result.getData().containsKey("checkOrder"));
        assertEquals(checkRecord, result.getData().get("checkOrder"));
    }

    @Test
    void changeState() {
        // 构造测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("state", 2);
        params.put("orderId", 1L);
        params.put("userId", 2L);
        params.put("remark", "test remark");

        // Mock the service layer to return data
        Mockito.when(checkService.changeState(2, 1L, 2L, "test remark")).thenReturn("修改成功");

        // Call the controller endpoint
        Result result = checkController.changeState(params);

        // Assert that the result message is "修改成功"
        assertEquals(result.getMessage(),"修改成功");
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertNotNull(result.getData());
    }
}