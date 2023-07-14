package com.example.bdd.purchase;

import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.OutRecord;
import com.example.entity.Task.PurchaseRecord;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class PurchaseStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private Result response = null;

    Map<String, Object> params = new HashMap<>();

    public PurchaseStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @假如("^我希望获取purchaseRecord$")
    public void i_want_purchaserecord() {
    }

    @当("^我使用type (\\d+) 调用getOrder$")
    public void i_call_getOrder_with_id(Integer type) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/purchase/purchaseOrder")
                        .param("type", type.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一组PurchaseRecord，type为 (\\d+)$")
    public void 返回的结果应为成功包含一组PurchaseRecordType为(Integer type) {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "返回成功");

        Map<String, Object> data = response.getData();
        assertNotNull(data.get("purchaseOrder"));

        List<LinkedHashMap<String, Object>> purchaseOrder = (List<LinkedHashMap<String, Object>>) data.get("purchaseOrder");
        for (LinkedHashMap<String, Object> map : purchaseOrder) {
            PurchaseRecord r = new ObjectMapper().convertValue(map, PurchaseRecord.class);
            assertEquals(r.getState(), type);
        }
    }

    @假如("^我有一个id为 (\\d+) 、operator为 (\\d+) 、remark为 \"([^\"]*)\" 的Map对象$")
    public void i_have_a_Map_object_with_id_Operator_Remark(Long id, Long operator, String remark) {
        params.put("id", id);
        params.put("operator", operator);
        params.put("remark", remark);
    }

    @当("^我使用该Map对象调用approveOrder$")
    public void i_call_the_approveorder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/purchase/approve")
                        .content(new ObjectMapper().writeValueAsString(params))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为采购单审核通过$")
    public void the_response_success() {
        assertEquals(ResultCode.SUCCESS,response.getCode());
        assertEquals("采购单审核通过",response.getMessage());
    }

    @假如("^我希望获取一个purchaseRecord$")
    public void i_want_a_null_purchaserecord() {
    }


    @当("^我通过type (\\d+) 调用getOrder$")
    public void i_use_type_call_getOrder(Integer type) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/purchase/purchaseOrder")
                        .param("type", type.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为失败$")
    public void the_response_fail() {
        ResultCode resultCode=new ResultCode();
        // 验证结果
        assertEquals(response.getCode(), ResultCode.InfoGetERR);
        assertEquals(response.getMessage(), resultCode.getMsg(ResultCode.InfoGetERR));
    }


    @假如("我使用一个id为 {int} 、operator为 {int} 、remark为 {string} 的Map对象")
    public void i_use_id_Operator_Remark_Map(int arg0, int arg1, String arg2) throws Exception {
        Long id=Long.valueOf(arg0);
        Long operator = Long.valueOf(arg1);
        String remark = arg2;
        params.put("id", id);
        params.put("operator", operator);
        params.put("remark", remark);
    }

    @当("我使用该Map对象调用rejectOrder")
    public void i_use_map_call_rejectorder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/purchase/reject")
                        .content(new ObjectMapper().writeValueAsString(params))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("返回的结果应为采购单审核未通过")
    public void response_reject() {
        assertEquals("采购单审核未通过",response.getMessage());
    }


    @假如("我有一个id为 {int} 、operator不合法，为 {int} 、remark为 {string} 的Map对象")
    public void i_id_Operator_Remark_Map(int arg0, int arg1, String arg2) {
        Long id=Long.valueOf(arg0);
        Long operator = Long.valueOf(arg1);
        String remark = arg2;
        params.put("id", id);
        params.put("operator", operator);
        params.put("remark", remark);
    }

    @当("使用该Map对象调用rejectOrder")
    public void map_reject_order() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/purchase/reject")
                        .content(new ObjectMapper().writeValueAsString(params))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("返回的结果为失败")
    public void response_is_fail() {
        ResultCode resultCode=new ResultCode();
        // 验证结果
        assertEquals(response.getCode(), ResultCode.ERROR);
        assertEquals(response.getMessage(), resultCode.getMsg(ResultCode.ERROR));
    }
}
