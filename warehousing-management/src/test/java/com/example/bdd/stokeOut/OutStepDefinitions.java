package com.example.bdd.stokeOut;

import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.OutRecord;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import net.minidev.json.JSONObject;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class OutStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private Result response = null;

    Map<String, Object> params = new HashMap<>();

    OutRecord outRecord = new OutRecord();

    public OutStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @假如("^我希望获取出库单列表$")
    public void i_want_outRecords() throws Exception {
    }

    @当("^我使用类型 (\\d+) 调用getOrder$")
    public void i_call_the_getOrder_endpoint_with_type_id(Integer type) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/stokeOut/outOrder")
                .param("type", type.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一组OutRecord，类型为 (\\d+),尺寸为 (\\d+)")
    public void the_response_should_contain_two_CheckRecords_with_IDs(Integer type, Integer num) {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "返回成功");

        Map<String, Object> data = response.getData();
        assertNotNull(data.get("outOrder"));

        List<LinkedHashMap<String, Object>> outOrder = (List<LinkedHashMap<String, Object>>) data.get("outOrder");
        for (LinkedHashMap<String, Object> map : outOrder) {
            OutRecord r = new ObjectMapper().convertValue(map, OutRecord.class);
            assertEquals(r.getState(), type);
        }
        assertEquals(outOrder.size(), num);
    }


    @假如("^我有一个id为 (\\d+) 、操作类型state为 (\\d+) 、operator为 (\\d+) 、remark为 \"([^\"]*)\" 的Map对象$")
    public void i_have_a_Map_object_with_orderId_realNum(Long id, Integer state, Long operator, String remark) {
        params.put("id", id);
        params.put("state", state);
        params.put("operator", operator);
        params.put("remark", remark);
    }

    @当("^我使用该Map对象调用changeState$")
    public void i_call_the_changeState() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeOut/changeState")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为拣货确认成功$")
    public void the_response_success() {
        assertEquals(ResultCode.SUCCESS,response.getCode());
        assertEquals("出库拣货确认",response.getMessage());
    }
}