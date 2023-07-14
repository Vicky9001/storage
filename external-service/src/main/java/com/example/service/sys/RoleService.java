package com.example.service.sys;

import com.example.entity.User.Role;
import com.example.mapper.sys.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)

public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    public List<Role> selectAll(){
        return roleMapper.selectAll();
    }

    public List<Role> select(String name){
        return roleMapper.select(name);
    }

    public int add(Role role){

        return roleMapper.add(role);
    }

    public int update(Role role) {

        return roleMapper.updateRole(role);
    }

    public int deleteById(Long id){

        return roleMapper.deleteByRoleId(id);
    }

    public int deleteByIds(List<Long> ids){
        return roleMapper.deleteByIds(ids);
    }
}
