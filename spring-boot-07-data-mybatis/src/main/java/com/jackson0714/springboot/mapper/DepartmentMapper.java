package com.jackson0714.springboot.mapper;

import com.jackson0714.springboot.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

// @Mapper
public interface DepartmentMapper {

    @Select("select * from department")
    List<Map<String, Object>> getAllDepartment();

    @Select("select * from department where id=#{id}")
    Department getDepartmentById(Long id);

    @Delete("delete from department where id=#{id}")
    int deleteDepartment(Long id);

    @Insert("insert into department(department_name) values(#{departmentName})")
    int createDepartment(String departmentName);

    @Update("update department set department_name=#{departmentName} where id=#{id}")
    int updateDepartmentById(Long id, String departmentName);
}
