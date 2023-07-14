package com.example.controller.sys;

import com.example.service.sys.UserRoleService;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class UserRoleController {
    @Resource
    UserRoleService userRoleService;
    ResultCode resultCode = new ResultCode();

    @PostMapping("/updateRoles")
    public Result updateUserRoles(@Validated @RequestBody Map<String,Object> params) {
        Integer userId = (Integer) params.get("userId");
        List<Integer> roleIds = (List<Integer>) params.get("roleIds");
        Result res = new Result();
        try {
            userRoleService.updateUserRoles(userId, roleIds);
            res.setMessage("角色更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }
}
