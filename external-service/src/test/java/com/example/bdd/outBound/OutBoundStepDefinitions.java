package com.example.bdd.outBound;

import com.alibaba.fastjson.JSON;
import com.example.entity.Task.OutRecord;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.gherkin.internal.com.eclipsesource.json.Json;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
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
public class OutBoundStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private Result response = null;

    Map<String, Object> params = new HashMap<>();

    OutRecord outRecord = new OutRecord();

    public OutBoundStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @假如("^我有一个creatorId为 (\\d+)、goodsId为 (\\d+)、num为 (\\d+)、logisticsType为 (\\d+)、描述为 \"([^\"]*)\" 的outRecord调拨对象$")
    public void i_have_a_outRecord(Integer creatorId, Integer goodsId, Integer num, Integer type,String desc) {
        outRecord.setCreatorId(Long.valueOf(creatorId));
        outRecord.setGoodsId(Long.valueOf(goodsId));
        outRecord.setNum(Float.valueOf(num));
        outRecord.setDesc(desc);
        outRecord.setLogisticsType(2);
    }


    @当("^我调用createOrder函数使用该调拨任务创建出库单$")
    public void i_call_the_createOrder() throws Exception {
        System.out.println("ss");
        System.out.println(String.valueOf(outRecord));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeOut/create")
                        .content(JSON.toJSONString(outRecord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一个outRecord$")
    public void the_response_should_contain_one_CheckRecord() {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "出库单创建成功");

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data.get("outOrder"));

        ObjectMapper objectMapper = new ObjectMapper();
        OutRecord resultRecords = objectMapper.convertValue(data.get("outOrder"), OutRecord.class);
        assertNotNull(resultRecords.getId());
    }
}