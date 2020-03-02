package com.jackson0714.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "DepartmentController", description = "部门controller")
@RequestMapping("/v1/client")
@RestController
public class DepartmentController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "查询所有部门")
    @GetMapping("/getAllDepartment")
    public List<Map<String, Object>> getAllDepartment() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from department");
        return list;
    }

    @ApiOperation(value = "根据id查询某个部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "需要查询的部门id")
    })
    @GetMapping("/{id}")
    public Map<String, Object> getDepartmentById(@PathVariable Long id) {
        String sql = "select * from department where id = " + id;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list.get(0);
    }

    @ApiOperation(value = "新增部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "部门名称")
    })
    @PostMapping("/create")
    public int createDepartment(@RequestParam String name) {
        String sql = String.format("insert into department(departmentName) value('%s')", name);
        int result = jdbcTemplate.update(sql);
        return result;
    }

    @ApiOperation(value = "根据id删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "需要删除的部门id")
    })
    @PostMapping("/delete")
    public int deleteDepartment(@RequestParam Long id) {
        String sql = String.format("delete from department where id = %d", id);
        int result = jdbcTemplate.update(sql);
        return result;
    }

    @ApiOperation(value = "根据id更新部门名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "需要更新的部门id"),
            @ApiImplicitParam(name = "name", value = "需要更新的部门名称")
    })
    @PostMapping("/update")
    public int updateDepartmentById(@RequestParam Long id, @RequestParam String name) {
        String sql = String.format("update department set departmentName = '%s' where id = %d", name, id);
        int result = jdbcTemplate.update(sql);
        return result;
    }
}
