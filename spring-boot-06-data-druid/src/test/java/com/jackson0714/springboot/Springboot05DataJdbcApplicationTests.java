package com.jackson0714.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@SpringBootTest
class Springboot05DataJdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        System.out.println("数据源：" + dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println("数据库连接：" + connection);
        connection.close();
    }

}
