package com.example.controller.sys;


import com.example.entity.User.User;
import com.example.service.sys.UserService;
import com.example.utils.JwtUtils;
import com.example.utils.Result;
import com.example.utils.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    ResultCode resultCode = new ResultCode();

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        System.out.println("username"+user.getUserName()+"password"+user.getPassword());
        try {
            User info = userService.LoginIn(user.getUserName(),user.getPassword());
            if(info!=null){
                String token = JwtUtils.generateToken(user.getUserName());
                map.put("token",token);
                res.setMessage("登录成功");
                res.setData(map);
            }else {
                res.setCode(ResultCode.LOGINERR);
                res.setMessage(resultCode.getMsg(ResultCode.LOGINERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }

        return res;
    }

    //通过id查询用户
    @GetMapping("/getUser")
    public User getUser(@RequestParam(name = "id")Long id){
        return userService.getUser(id);
    }

    @GetMapping("/info")  // "token:xxx"
    public Result info(String token){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            String username = JwtUtils.getClaimsByToken(token).getSubject();
            List<User> users = userService.getUser(username, null, null);
            User user = users.get(0);
            if(user!=null){
                String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.gmz88.com%2Fuploadimg%2Fimage%2F20190116%2F15476240655c3ede81c64116.77854307.jpeg&refer=http%3A%2F%2Fimg.gmz88.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1667038105&t=0374e9cdf9ecb5833b85cbe9bae9a4c8";
                map.put("userId",user.getUserId());
                map.put("userName",username);
                map.put("realName",user.getRealName());
                map.put("phone",user.getPhone());
                map.put("password",user.getPassword());
                map.put("roles",user.getRoles());
                map.put("avatar",url);
                res.setMessage("返回成功");
                res.setData(map);
            }else {
                res.setCode(ResultCode.INFOERR);
                res.setMessage(resultCode.getMsg(ResultCode.INFOERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }
        return res;
    }

    @PostMapping("/logout")  // "token:xxx"
    public Result logout(){
        Result res = new Result();
        res.setMessage("登出成功");
        return res;
    }

    //  用户注册
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        System.out.println("user"+user);
        Result res = new Result();
        try {
            int r = userService.addUser(user);
            if(r > 0){
                res.setMessage("注册成功");
            }else{
                res.setCode(ResultCode.REGERR);
                res.setMessage(resultCode.getMsg(ResultCode.REGERR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ResultCode.ERROR);
            res.setMessage(resultCode.getMsg(ResultCode.ERROR));
        }

        return res;
    }

    // 更新用户信息
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        Result res = new Result();
        System.out.println(user.toString());
        try {
            int r = userService.update(user);
            if(r > 0){
                res.setMessage("用户信息更新成功");
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

    // 用户查询
    @GetMapping("/userlist")
    public Result queryUserList(@RequestParam(required = false,name = "userName") String userName,
                                @RequestParam(required = false,name = "realName") String realName,
                                @RequestParam(required = false,name = "roles") Long roleId){
        Result res = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            List<User> list = userService.getUser(userName,realName,roleId);
            // 去除重复查询
            list = list.stream().distinct().collect(Collectors.toList());
            map.put("userlist",list);
            res.setMessage("返回成功");
            res.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(resultCode.ERROR);
            res.setMessage(resultCode.getMsg(resultCode.ERROR));
        }
        return res;
    }

    //增加用户
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        System.out.println("user"+user);
        Result res = new Result();
        try {
            int r = userService.addUser(user);
            if(r > 0){
                res.setMessage("新增用户成功");
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

    //批量删除用户
    @DeleteMapping("/delete")
    public Result deleteUser(@RequestBody List<User> userList){
        Result res = new Result();
        res.setMessage("删除用户成功");
        try {
            for (User user:userList) {
                int r = userService.deleteUser(user);
                if( r <= 0) {
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
