package com.example.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User.Role;
import com.example.entity.User.User;
import com.example.mapper.sys.RoleMapper;
import com.example.mapper.sys.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)

public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    public User LoginIn(String username, String password) {
        System.out.println("username"+username+"password"+password);
        return userMapper.getInfo(username,password);
    }

    public User getInfoByName(String username) {
        return userMapper.selectByName(username);
    }
    public int addUser(User user){
        System.out.println("user"+user);
        User info = userMapper.selectByName(user.getUserName());
        if(info==null){
            return userMapper.insert(user);
        }else return -1;
    }

    public int update(User user){
        return userMapper.updateById(user);
    }

    public List<User> getUser(String userName, String realName, Long roleId) {
        List<User> userList = userMapper.getUserList(userName, realName, roleId);
        for (User user : userList) {
            List<Role> roleList = roleMapper.selectList(new QueryWrapper<Role>().inSql("role_id", "SELECT role_id FROM user_role WHERE user_id = " + user.getUserId()));
            user.setRoles(roleList);
        }
        return userList;
    }

    public int deleteUser(User user) {
        userMapper.deleteRoleByUserId(user.getUserId());
        return userMapper.deleteById(user.getUserId());
    }

    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
}
