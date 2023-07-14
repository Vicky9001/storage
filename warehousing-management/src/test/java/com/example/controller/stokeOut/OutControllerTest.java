package com.example.controller.stokeOut;

import com.example.controller.check.CheckController;
import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.OutRecord;
import com.example.service.check.CheckService;
import com.example.service.stokeOut.OutService;
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
class OutControllerTest {

    @Mock
    private OutService outService;

    @InjectMocks
    private OutController outController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrder() {
        // Prepare test data
        List<Integer> type = Arrays.asList(1);
        List<OutRecord> records = new ArrayList<>();
        OutRecord record = new OutRecord();
        record.setDesc("test");
        records.add(record);
        // Mock the service layer to return data
        Mockito.when(outService.getList(type)).thenReturn(records);

        // Call the controller endpoint
        Result result = outController.getOrder(type);

        // Verify the results
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertEquals(result.getMessage(), "返回成功");
        assertNotNull(result.getData());

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertNotNull(data.get("outOrder"));

        List<CheckRecord> resultRecords = (List<CheckRecord>) (data.get("outOrder"));
        assertEquals(resultRecords.size(), 1);
        assertEquals(resultRecords.get(0),record);
    }

    @Test
    void createOrder() {
        // 构造测试数据
        OutRecord record = new OutRecord();
        record.setDesc("test");
        Mockito.when(outService.create(Mockito.any())).thenReturn(record);
        // Call the controller endpoint
        OutRecord result = outController.createOrder(new OutRecord());

        // Assert that the result message
        assertEquals(result.getDesc(), "test");
    }

    @Test
    void changeState() {
        // 构造测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("state", 16);
        params.put("id", 1L);
        params.put("operator", 2L);
        params.put("remark", "test");

        // Mock the service layer to return data
        Mockito.when(outService.changeState(16, 1L, 2L, "test")).thenReturn("出库拣货确认");

        // Call the controller endpoint
        Result result = outController.changeState(params);

        // Assert that the result message is "修改成功"
        assertEquals(result.getMessage(),"出库拣货确认");
        assertEquals(result.getCode(), ResultCode.SUCCESS);
        assertNotNull(result.getData());
    }
}