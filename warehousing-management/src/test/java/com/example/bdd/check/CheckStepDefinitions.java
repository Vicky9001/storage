package com.example.bdd.check;

import com.example.controller.check.CheckController;
import com.example.entity.Task.CheckRecord;
import com.example.service.check.CheckService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class CheckStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private Result response = null;

    Map<String, Object> params = new HashMap<>();

    public CheckStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @假如("^我有一组CheckRecords$")
    public void i_have_a_list_of_CheckRecords() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/check/checkOrder")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()) //断言HTTP响应的状态码是否为200 OK
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
        Map<String, Object> data = (Map<String, Object>) response.getData();
        // 断言checkOrder列不为空
        assertNotNull(data.get("checkOrder"));

        // 断言checkOrder列中是否包含了checkOrder对象
        List<LinkedHashMap<String, Object>> checkRecords = (List<LinkedHashMap<String, Object>>) data.get("checkOrder");
        List<CheckRecord> checkRecordList = new ArrayList<>();
        for (LinkedHashMap<String, Object> checkRecordMap : checkRecords) {
            CheckRecord checkRecord = new ObjectMapper().convertValue(checkRecordMap, CheckRecord.class);
            checkRecordList.add(checkRecord);
            assertTrue(checkRecordList.contains(checkRecord));
        }
    }

    @当("^我使用类型 (\\d+) 和创建人ID (\\d+) 调用getOrder$")
    public void i_call_the_getOrder_endpoint_with_type_id(Integer type, Long id) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/check/checkOrder")
                .param("type", type.toString())
                .param("id", id.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一组CheckRecord，类型为 (\\d+)，创建人ID为 (\\d+)")
    public void the_response_should_contain_two_CheckRecords_with_IDs(Integer type, Long id) {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "返回成功");

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data.get("checkOrder"));

        List<LinkedHashMap<String, Object>> checkRecords = (List<LinkedHashMap<String, Object>>) data.get("checkOrder");
        for (LinkedHashMap<String, Object> checkRecordMap : checkRecords) {
            CheckRecord checkRecord = new ObjectMapper().convertValue(checkRecordMap, CheckRecord.class);
            assertEquals(checkRecord.getState(), type);
            assertEquals(checkRecord.getCreatorId(), id);
        }
    }

    @假如("^我有一个creatorId为 (\\d+)、positionId为 (\\d+)、描述为 \"([^\"]*)\" 的Map对象$")
    public void i_have_a_Map_object_with_creatorId_positionId_desc(Long creatorId, Long positionId, String desc) {
        params.put("creatorId", creatorId);
        params.put("positionId", positionId);
        params.put("desc", desc);
    }

    @当("^我使用该Map对象调用createOrder$")
    public void i_call_the_createOrder_endpoint_with_Map_object() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/check/create")
                .content(JSONObject.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一个CheckRecord$")
    public void the_response_should_contain_one_CheckRecord() {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "盘点单创建成功");

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data.get("checkOrder"));

        ObjectMapper objectMapper = new ObjectMapper();
        CheckRecord resultRecords = objectMapper.convertValue(data.get("checkOrder"), CheckRecord.class);
        assertNotNull(resultRecords.getId());
    }

    @假如("^我有一个orderId为 (\\d+) 、盘点结果realNum为 ([\\d\\.]+)的Map对象$")
    public void i_have_a_Map_object_with_orderId_realNum(Long orderId, Float realNum) {
        params.put("orderId", orderId);
        params.put("realNum", realNum);
    }

    @当("^我使用该Map对象调用updateOrder$")
    public void i_call_the_updateOrder_API_with_Map_object() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/check/update")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一个更新后的CheckRecord$")
    public void the_response_should_contain_one_updated_CheckRecord() {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "盘点数据更新成功");

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data.get("checkOrder"));

        ObjectMapper objectMapper = new ObjectMapper();
        CheckRecord resultRecords = objectMapper.convertValue(data.get("checkOrder"), CheckRecord.class);
        assertNotNull(resultRecords.getId());
        assertNotNull(resultRecords.getDiffPrice());
    }

    @假如("^我输入的realNum不合法，比如orderId为 (\\d+) 、realNum超出货架容量 (\\d+)$")
    public void i_have_a_illegal_Map_object_with_orderId_realNum(Long orderId, Float realNum) {
        params.put("orderId", orderId);
        params.put("realNum", realNum);
    }

    @当("^我使用这些不合法的参数调用updateOrder$")
    public void i_call_the_updateOrder_API_with_illegal_Map_object() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/check/update")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为失败，包含一个错误提示信息$")
    public void the_response_should_contain_one_updated_error_Message() {
        assertEquals(response.getCode(), ResultCode.ERROR);
        assertEquals(response.getMessage(), "统计数量大于货架容量，请检查统计数据并重新录入");
    }
}