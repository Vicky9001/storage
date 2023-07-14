package com.example.service.info;


import com.example.entity.Info.Department;
import com.example.mapper.info.DepartmentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private DepartmentMapper mapper;
    public List<Department> getInfo(String name) {
        List<Department> departments =  mapper.getDepartment(name);
        return departments;
    }

    public int addDepartment(Department department){
        Department info = mapper.selectByName(department.getDepartmentName());
        if(info==null){
            return mapper.insert(department);
        }else return -1;
    }
    public int update(Department department){
        return mapper.updateById(department);
    }

    public int delete(Department department) {
        return mapper.deleteById(department.getDepartmentId());
    }

    public Department getDepartment(Long id) {
        return mapper.selectById(id);
    }
}
