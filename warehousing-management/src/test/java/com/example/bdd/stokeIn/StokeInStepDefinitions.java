package com.example.bdd.stokeIn;

import com.example.controller.stokeIn.WarehousingController;
import com.example.entity.Task.CheckRecord;
import com.example.entity.Task.GoodsBatch;
import com.example.entity.Task.WarehousingRecord;
import com.example.service.stokeIn.WarehousingService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class StokeInStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    private Result response = null;

    Map<String, Object> params = new HashMap<>();

    /*private WarehousingRecord warehousingRecord;*/

    public StokeInStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    // Step: 当我使用创建人ID为 1、描述为 "test"、物资批次id为 53 的 20 瓶消毒液放入货区id为 8 的C类仓库暂存区
    @当("^我使用创建人ID为 (\\d+)、描述为 \"([^\"]*)\"、物资批次id为 (\\d+) 的 (\\d+) 瓶消毒液放入货区id为 (\\d+) 的C类仓库存储区$")
    public void i_place_disinfectant_into_temporary_area_with_creator_id_desc_batch_id_quantity_area_id(Integer creatorId, String desc, Integer batchId, Integer quantity, Integer areaId) throws Exception {
        // Prepare the request body
        params.put("creatorId", creatorId);
        params.put("desc", desc);

        List<Map<String, Object>> goodsBatches = new ArrayList<>();
        Map<String, Object> goodsBatch = new HashMap<>();
        goodsBatch.put("id", batchId);
        goodsBatch.put("areaId", areaId);
        goodsBatch.put("delnum", quantity);
        goodsBatches.add(goodsBatch);

        params.put("goodsBatches", goodsBatches);

        // Send the request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeIn/createWarehousing")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Process the response
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    // Step: 那么返回的结果应为成功，包含入库位置信息
    @那么("^返回的结果应为成功，包含入库位置信息$")
    public void the_response_should_contain_successful_message_with_storage_location() {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertNotNull(response.getMessage());
    }

    // Step: 当我使用创建人ID为 1、描述为 "test"、物资批次id为 53 的 9999 瓶消毒液（超出采购数量）放入货区id为 8 的C类仓库暂存区
    @当("^我使用创建人ID为 (\\d+)、描述为 \"([^\"]*)\"、物资批次id为 (\\d+) 的 (\\d+) 瓶消毒液（超出采购数量）放入货区id为 (\\d+) 的C类仓库存储区$")
    public void i_place_excessive_disinfectant_into_temporary_area_with_creator_id_desc_batch_id_quantity_area_id(Integer creatorId, String desc, Integer batchId, Integer quantity, Integer areaId) throws Exception {
        // Prepare the request body
        params.put("creatorId", creatorId);
        params.put("desc", desc);

        List<Map<String, Object>> goodsBatches = new ArrayList<>();
        Map<String, Object> goodsBatch = new HashMap<>();
        goodsBatch.put("id", batchId);
        goodsBatch.put("areaId", areaId);
        goodsBatch.put("delnum", quantity);
        goodsBatches.add(goodsBatch);

        params.put("goodsBatches", goodsBatches);

        // Send the request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeIn/createWarehousing")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Process the response
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    // Step: 那么返回的结果应为失败，提示入库数量异常
    @那么("^返回的结果应为失败，提示入库数量异常$")
    public void the_response_should_contain_failed_message_with_abnormal_storage_quantity() {
        assertEquals(response.getCode(), ResultCode.INVALID_PARAMETER);
        assertEquals(response.getMessage(), "操作失败：物资名称：消毒液，入库数量不能大于待入库数量");
    }


    // Step: 当我使用创建人ID为 1、描述为 "test"、物资批次id为 53 的 0.001 瓶消毒液放入货区id为 8 的C类仓库存储区
    @当("^我使用创建人ID为 (\\d+)、描述为 \"([^\"]*)\"、物资批次id为 (\\d+) 的 ([\\d\\.]+) 瓶消毒液放入货区id为 (\\d+) 的C类仓库存储区$")
    public void i_place_disinfectant_into_storage_area_with_creator_id_desc_batch_id_quantity_area_id(Integer creatorId, String desc, Integer batchId, Float quantity, Integer areaId) throws Exception {
        // Prepare the request body
        params.put("creatorId", creatorId);
        params.put("desc", desc);

        List<Map<String, Object>> goodsBatches = new ArrayList<>();
        Map<String, Object> goodsBatch = new HashMap<>();
        goodsBatch.put("id", batchId);
        goodsBatch.put("areaId", areaId);
        goodsBatch.put("delnum", quantity);
        goodsBatches.add(goodsBatch);

        params.put("goodsBatches", goodsBatches);

        // Send the request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeIn/createWarehousing")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Process the response
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    // Step: 那么返回的结果应为失败，提示入库数量格式错误
    @那么("^返回的结果应为失败，提示入库数量格式错误$")
    public void the_response_should_contain_failure_message_with_invalid_quantity_format() {
        assertEquals(response.getCode(), ResultCode.INVALID_PARAMETER);
        assertEquals(response.getMessage(), "操作失败：物资名称：消毒液，入库数量必须为整数");
    }

    // Step: 当我使用创建人ID为 1、描述为 "test"、物资批次id为 53 的 500 瓶消毒液放入货区id为 8 的C类仓库存储区
    @当("^我使用创建人ID为 (\\d+)、描述为 \"([^\"]*)\"、物资批次id为 (\\d+) 的 (\\d+) 瓶消毒液放入货区id为 (\\d+) 的C类仓库存储区$")
    public void i_place_disinfectant_into_storage_area_with_creator_id_desc_batch_id_quantity_area_id(Integer creatorId, String desc, Integer batchId, Integer quantity, Integer areaId) throws Exception {
        // Prepare the request body
        params.put("creatorId", creatorId);
        params.put("desc", desc);

        List<Map<String, Object>> goodsBatches = new ArrayList<>();
        Map<String, Object> goodsBatch = new HashMap<>();
        goodsBatch.put("id", batchId);
        goodsBatch.put("areaId", areaId);
        goodsBatch.put("delnum", quantity);
        goodsBatches.add(goodsBatch);

        params.put("goodsBatches", goodsBatches);

        // Send the request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeIn/createWarehousing")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Process the response
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    // Step: 那么返回的结果应为失败，提示仓库位置已满
    @那么("^返回的结果应为失败，提示仓库位置已满$")
    public void the_response_should_contain_failure_message_with_storage_area_full() {
        assertEquals(response.getCode(), ResultCode.SERVER_ERROR);
        assertEquals(response.getMessage(), "操作失败：仓库余位不足，入库单创建失败，请修改物资入库数量或联系管理员\n物资名称：消毒液");
    }

    @当("^我使用类型 (\\d+) 和创建人ID (\\d+) 和货物ID (\\d+) 和供应商ID (\\d+) 调用 getOrderList$")
    public void i_call_the_getOrderList_endpoint_with_type_id(Integer state, Long id,Long goodsId,Long supplier) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/stokeIn/warehousingOrder")
                .param("state", state.toString())
                .param("id", id.toString())
                .param("goodsId", goodsId.toString())
                .param("supplier", supplier.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为成功，包含一组WarehousingRecord，类型为 (\\d+)，创建人ID为 (\\d+)")
    public void the_response_should_contain_two_WarehousingRecords_with_IDs(Integer state,Long id) {
        assertEquals(response.getCode(), ResultCode.SUCCESS);
        assertEquals(response.getMessage(), "返回成功");

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data.get("WarehousingOrder"));

        List<LinkedHashMap<String, Object>> warehousingRecords = (List<LinkedHashMap<String, Object>>) data.get("WarehousingOrder");
        for (LinkedHashMap<String, Object> warehousingRecordMap : warehousingRecords) {
            WarehousingRecord warehousingRecord = new ObjectMapper().convertValue(warehousingRecordMap, WarehousingRecord.class);
            assertEquals(warehousingRecord.getState(), state);
            assertEquals(warehousingRecord.getCreatorId(), id);
        }
    }
    @假如("^我有一个入库单号 (\\d+) 、操作类型 (\\d+) 、审批人ID (\\d+) 、备注 \"([^\"]*)\" 的Map对象$")
    public void i_have_a_Map_object_with_orderId_state(Long orderId, Integer state, Long userId, String remark) {
        params.put("orderId", orderId);
        params.put("state", state);
        params.put("userId", userId);
        params.put("remark", remark);
    }

    @当("^我使用该Map对象调用changeState$")
    public void i_call_the_changeState() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/stokeIn/changeState")
                .content(new ObjectMapper().writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        response = new ObjectMapper().readValue(responseBody, Result.class);
    }

    @那么("^返回的结果应为验收确认成功$")
    public void the_response_success() {
        assertEquals(ResultCode.SUCCESS,response.getCode());
        assertEquals("入库单状态修改成功",response.getMessage());
    }
}
