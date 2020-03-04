package com.jackson0714.springboot.mapper;

import com.jackson0714.springboot.entity.User;

// @Mapper 或MapperScan 将接口扫描装配到装配容器中
public interface UserMapper {
    User getUserById(Long userId);
}
