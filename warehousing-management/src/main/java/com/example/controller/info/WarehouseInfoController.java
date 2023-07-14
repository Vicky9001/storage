package com.example.controller.info;


import com.example.entity.Info.Area;
import com.example.entity.Info.Position;
import com.example.service.info.AreaService;
import com.example.service.info.PositionService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/position")
public class WarehouseInfoController {

    @Resource
    AreaService areaService;
    @Resource
    PositionService positionService;
    ResultCode resultCode = new ResultCode();

    // 更新货位信息
    @PostMapping("/updatePositions")  // "token:xxx"
    public void updatePositions(@RequestBody List<Position> list){
        positionService.updatePositions(list);
    }

    // 根据货位id查找货位
    @GetMapping("/findPosition")  // "token:xxx"
    public Position findPosition(@RequestParam(name = "id") Long id){
        return positionService.findPosition(id);
    }

    // 根据货区id、货区类型、货位类型和状态查找货位
    @GetMapping("/findPositions")  // "token:xxx"
    public List<Position> findPositions(@RequestParam(required = false, name = "areaId") Long areaId,
                                        @RequestParam(required = false, name = "areaType") Integer areaType,
                                        @RequestParam(required = false, name = "goodsType") Integer goodsType,
                                        @RequestParam(required = false, name = "state") Integer state){
        return positionService.findPositions(areaId, areaType, goodsType, state);
    }

    // 根据货物类型（goodsId）、货区类型查找货位
    @GetMapping("/findOutPositions")  // "token:xxx"
    List<Position> findOutPositions(@RequestParam(required = false, name = "goodsId") Long goodsId,
                                    @RequestParam(required = false, name = "areaType") Integer areaType){
        return positionService.findOutPositions(goodsId, areaType);
    }

    // 根据物资批次id查找货位
    @GetMapping("/findPositionByGoods")  // "token:xxx"
    public List<Position> findPositionByGoods(@RequestParam(name = "id") Long id){
        return positionService.findPositionByGoods(id);
    }

    //得到仓库名
    @GetMapping("/houseList")  // "token:xxx"
    public Result getHouseName(){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<String> list = areaService.getHouse();
            map.put("houseList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    @GetMapping("/findAreaList")  // "token:xxx"
    List<Area> findArea(@RequestParam(required = false,name = "areaId") Long areaId,
                        @RequestParam(required = false,name = "areaType") Integer areaType,
                        @RequestParam(required = false,name = "houseType") Integer houseType){
        return areaService.getAreaByType(areaId, areaType,houseType);
    }

    //得到货区信息
    @GetMapping("/areaList")  // "token:xxx"
    public Result getArea(@RequestParam(required = false,name = "id") Long id,
                          @RequestParam(required = false,name = "name") String name,
                          @RequestParam(required = false,name = "goodsType") Integer goodsType){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Area> list = areaService.getInfo(id,name,goodsType);
            map.put("areaList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }
    //  增加货区
    @PostMapping("/addArea")
    public Result addArea(@Validated @RequestBody  Map<String,Object> params){
        String houseName = (String) params.get("houseName");
        Integer houseType = (Integer) params.get("houseType");
        List<Integer> area_type = (List<Integer>) params.get("areaType");
        Result res = new Result();
        try {
            int r = areaService.addArea(houseName, houseType, area_type);
            if(r > 0){
                res.setMessage("添加货区成功");
            }else{
                res.setCode(ResultCode.EXISTERR);
                res.setMessage(resultCode.getMsg(ResultCode.EXISTERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }

        return res;
    }
    // 删除货区信息
    @DeleteMapping("/delArea")
    public Result delArea(@RequestBody List<Area> areaList){
        Result res = new Result();
        res.setMessage("信息删除成功");
        try {
            for (Area a : areaList) {
                System.out.println("Area:"+a);
                Long area_id = areaService.getId(a);
                int r1 = positionService.check(area_id);
                if(r1 < 0){
                    res.setCode(ResultCode.HouseERR);
                    res.setMessage(resultCode.getMsg(ResultCode.HouseERR));
                    return res;
                }
                int r2 = positionService.deleteArea(area_id);
                int r3 = areaService.delete(a);
                System.out.println("r2:"+r2+"r3:"+r3);
                if(r2 < 0 || r3 < 0){
                    res.setCode(ResultCode.InfoDelERR);
                    res.setMessage(resultCode.getMsg(ResultCode.InfoDelERR));
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @GetMapping("/positionList")  // "token:xxx"
    public Result getPosition(@RequestParam(required = false,name = "id") Long id,
                              @RequestParam(required = false,name = "state") Integer state){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Position> list = positionService.getInfo(id, state);
            map.put("positionList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    // 根据货区id、货区类型、物资类型（goodsId）、物资批次、状态查找货位 用于物资盘点
    @GetMapping("/searchPositions")  // "token:xxx"
    public Result searchPositions(@RequestParam(required = false, name = "areaId") Long areaId,
                                  @RequestParam(required = false, name = "areaType") Integer areaType,
                                  @RequestParam(required = false, name = "goodsId") Long goodsId,
                                  @RequestParam(required = false, name = "batchInfo") Long batchInfo,
                                  @RequestParam(required = false, name = "state") Integer state){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Position> list = positionService.searchPositions(areaId, areaType, goodsId, batchInfo, state);
            map.put("positionList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }

    //  增加货架位置
    @PostMapping("/addPosition")
    public Result addPosition(@Validated @RequestBody  Map<String,Object> params){
        Long area_id = Long.parseLong(params.get("areaId").toString());
        Long goods_type = null;
        if(params.get("goodsType")!=null)
             goods_type = Long.parseLong(params.get("goodsType").toString());
        Result res = new Result();
        try {
            int r = positionService.addPosition(area_id,goods_type);
            if(r > 0){
                res.setMessage("添加货架成功");
            }else{
                res.setCode(ResultCode.EXISTERR);
                res.setMessage(resultCode.getMsg(ResultCode.EXISTERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }

        return res;
    }

    //修改货架信息（一般修改货区和货物类型？）
    @PutMapping("/modPosition")
    public Result modPosition(@RequestBody Position position){
        Result res = new Result();
        try {
            int r = positionService.update(position);
            if(r > 0){
                res.setMessage("信息更新成功");
            }else{
                res.setCode(ResultCode.InfoModERR);
                res.setMessage(resultCode.getMsg(ResultCode.InfoModERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
    // 删除货架信息
    @DeleteMapping("/delPosition")
    public Result delPosition(@RequestBody List<Position> positionList){
        Result res = new Result();
        res.setMessage("信息删除成功");
        try {
            for (Position p : positionList) {
                int r = positionService.delete(p);
                if(r <= 0){
                    res.setCode(ResultCode.InfoDelERR);
                    res.setMessage(resultCode.getMsg(ResultCode.InfoDelERR));
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
}
