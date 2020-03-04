package com.jackson0714.springboot.controller;

import com.jackson0714.springboot.entity.User;
import com.jackson0714.springboot.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "UserController", description = "用户controller")
@RequestMapping("/v1/client")
@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @ApiOperation(value = "1.根据id查询某个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "需要查询的用户userId", value = "需要查询的用户userId")
    })
    @GetMapping("/emp/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return userMapper.getUserById(userId);
    }
}
