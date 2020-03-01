# 每天3分钟玩转SpringBoot - 05. 数据访问之JDBC

## 一、JDBC是什么？

JDBC API 属于Java APIJDBC用于以下几种功能：连接到数据库、执行SQL语句

## 二、Spring Boot中如何使用JDBC

### 1.创建 Spring Boot Project 时引入 JDBC API 依赖和 MySQL Driver依赖

![img](.\images\spring-boot-05-data-jdbc\1.png)
可以在POM中找到引入的JDBC依赖和mysql依赖：
JDBC 依赖：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

MySql 驱动依赖：

``` xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```

### 2.配置数据库连接

新增配置文件：src/main/resources/application.yml

``` yaml
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/study-spring-boot?serverTimezone=UTC&useUnicode=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf-8
    driverClassName: com.mysql.cj.jdbc.Driver
```

注意：`com.mysq.jdbc.Driver` 被废弃了，需要使用`com.mysql.cj.jdbc.Driver`

### 3.查看使用的数据源和数据库连接

``` java
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
	DataSource dataSource; //自动配置数据源，使用yml配置

	@Test
	void contextLoads() throws SQLException {
		System.out.println("数据源：" + dataSource.getClass());

		Connection connection = dataSource.getConnection();
		System.out.println("数据库连接：" + connection);
		connection.close();
	}

}

```

数据源：class com.zaxxer.hikari.HikariDataSource

数据库连接：HikariProxyConnection@1335157064 wrapping com.mysql.cj.jdbc.ConnectionImpl@7ff8a9dc

![img](.\images\spring-boot-05-data-jdbc\6.png)

## 三、原理





# 报错：

### 1.java.sql.SQLException:null, message from server: "Host 'Siri' is not allowed to connect to this MySQL server"

![img](.\images\spring-boot-05-data-jdbc\2.png)

解决方案：

执行命令：

``` shell
use mysql;
select host from user;
update user set host = '%' where user = 'root'
```

执行结果：

``` shell
Query OK, 1 row affected
```

如下图所示：

![img](.\images\spring-boot-05-data-jdbc\4.png)

### 2.Caused by: com.mysql.cj.exceptions.InvalidConnectionAttributeException: The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.

![img](.\images\spring-boot-05-data-jdbc\3.png)

解决方案：

配置spring.datasource.url 时，增加参数：serverTimezone=UTC

![img](.\images\spring-boot-05-data-jdbc\5.png)

