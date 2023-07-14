package com.example.controller.info;


import com.example.entity.Info.Department;
import com.example.service.info.DepartmentService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    DepartmentService service;

    ResultCode resultCode = new ResultCode();

    @GetMapping("/departmentList")  // "token:xxx"
    public Result getDepartment(@RequestParam(required = false,name = "name") String name){
        Result res = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Department> list = service.getInfo(name);
            map.put("departmentList",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.InfoGetERR);
            res.setMessage(resultCode.getMsg(resultCode.InfoGetERR));
        }
        return res;
    }
    //  增加供应商
    @PostMapping("/addDepartment")
    public Result addDepartment(@RequestBody Department department){
        Result res = new Result();
        try {
            int r = service.addDepartment(department);
            if(r > 0){
                res.setMessage("添加科室成功");
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

    // 更新供应商信息
    @PutMapping("/modDepartment")
    public Result modDepartment(@RequestBody Department department){
        Result res = new Result();
        try {
            int r = service.update(department);
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
    // 删除供应商信息
    @DeleteMapping("/delDepartment")
    public Result delDepartment(@RequestBody List<Department> departmentList){
        Result res = new Result();
        res.setMessage("信息删除成功");
        try {
            for (Department c : departmentList) {
                int r = service.delete(c);
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

    @GetMapping("/findDepartment")
    Department findDepartment(@RequestParam(name = "departmentId") Long id) {
        return service.getDepartment(id);
    }
}
