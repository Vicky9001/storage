package com.example.controller.sys;


import com.example.entity.User.Role;
import com.example.service.sys.RoleService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    RoleService roleService;
    ResultCode resultCode = new ResultCode();

    //获取所有角色列表
    @GetMapping("/roleList")
    public Result getRoleList(){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<Role> roleList=roleService.selectAll();
            map.put("roleList",roleList);
            res.setMessage("成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    //获取查询角色列表
    @GetMapping("/searchRoleList")
    public Result searchRoleList(@RequestParam(required = false,name = "name") String name){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<Role> roleList=roleService.select(name);
            map.put("roleList",roleList);
            res.setMessage("成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    // 更新角色信息
    @PutMapping("/updateRole")
    public Result update(@RequestBody Role role){
        Result res = new Result();
        try {
            int r = roleService.update(role);
            if(r > 0){
                res.setMessage("角色信息更新成功");
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


    //增加角色
    @PostMapping("/addRole")
    public Result addRole(@RequestBody Role role){
        Result res = new Result();
        try {
            int r = roleService.add(role);
            if(r > 0){
                res.setMessage("新增角色成功");
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

    //单删角色
    @DeleteMapping("/deleteRole/{id}")
    public Result deleteUser(@PathVariable("id") Long id){
        Result res = new Result();
        try {
            roleService.deleteById(id);
            int r = roleService.deleteById(id);
            if(r>0){
                res.setMessage("删除角色成功");
            }else{
                res.setCode(ResultCode.InfoDelERR);
                res.setMessage(resultCode.getMsg(ResultCode.InfoDelERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    //批量删除
    @DeleteMapping("/deleteRoles/{ids}")
    public Result deleteUser(@PathVariable("ids") List<Long> ids){
        Result res = new Result();
        try {
            roleService.deleteByIds(ids);
            int r = roleService.deleteByIds(ids);
            if(r>0){
                res.setMessage("删除角色成功");
            }else{
                res.setCode(ResultCode.InfoDelERR);
                res.setMessage(resultCode.getMsg(ResultCode.InfoDelERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
}
