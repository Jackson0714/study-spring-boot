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

默认数据源：class com.zaxxer.hikari.HikariDataSource

数据库连接：HikariProxyConnection@1335157064 wrapping com.mysql.cj.jdbc.ConnectionImpl@7ff8a9dc

![img](.\images\spring-boot-05-data-jdbc\6.png)

## 三、自动配置原理

自动配置文件路径：org.springframework.boot.autoconfigure.jdbc

DataSourceConfiguration用来自动导入数据源（根据各种判断）

``` java
/**
	 * Tomcat Pool DataSource configuration.
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
	@ConditionalOnMissingBean(DataSource.class)
	@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "org.apache.tomcat.jdbc.pool.DataSource",
			matchIfMissing = true)
	static class Tomcat {

		@Bean
		@ConfigurationProperties(prefix = "spring.datasource.tomcat")
```

1）如果导入了org.apache.tomcat.jdbc.pool.DataSource数据源，并且配置的spring.datasource.type配置的是org.apache.tomcat.jdbc.pool.DataSource，如果没配置type则使用tomcat数据源

2）HikariDataSource数据源也类似这样判断。

3）默认使用tomcat数据源

4）默认支持以下数据源

``` java
org.apache.tomcat.jdbc.pool、HikariDataSource、org.apache.commons.dbcp2
```

5）支持自定义数据源

使用DataSourceBuilder创建数据源，利用反射创建响应type的数据源，并且绑定相关属性

``` java
	/**
	 * Generic DataSource configuration.
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(DataSource.class)
	@ConditionalOnProperty(name = "spring.datasource.type")
	static class Generic {

		@Bean
		DataSource dataSource(DataSourceProperties properties) {
          //使用DataSourceBuilder创建数据源，利用反射创建响应type的数据源，并且绑定相关属性
			return properties.initializeDataSourceBuilder().build();
		}

	}
```

6）DataSourceInitializerInvoker 运行脚本

```java
/**
 * Bean to handle {@link DataSource} initialization by running {@literal schema-*.sql} on
 * {@link InitializingBean#afterPropertiesSet()} and {@literal data-*.sql} SQL scripts on
 * a {@link DataSourceSchemaCreatedEvent}.
 *
 * @author Stephane Nicoll
 * @see DataSourceAutoConfiguration
 */
class DataSourceInitializerInvoker implements ApplicationListener<DataSourceSchemaCreatedEvent>, InitializingBean {
```

```java
createSchema() 创建表 （文件名规则 schema-*.sql）
initSchema() 执行数据脚本 （文件名规则 data-*.sql）
```

getScripts() 来获取需要执行的脚本

``` java
private List<Resource> getScripts(String propertyName, List<String> resources, String fallback) {
  if (resources != null) {
    return getResources(propertyName, resources, true);
  }
  String platform = this.properties.getPlatform();
  List<String> fallbackResources = new ArrayList<>();
  fallbackResources.add("classpath*:" + fallback + "-" + platform + ".sql");
  fallbackResources.add("classpath*:" + fallback + ".sql");
  return getResources(propertyName, fallbackResources, false);
}
```

1.`fallback` = "schema", `platform`="all",会自动执行根目录下：schema-all.sql 或schema.sql 文件

2.fallback = "data", `platform`="all",会自动执行根目录下：data-all.sql 或data.sql 文件

isEnabled() 方法判断是否开启了自动执行脚本

有三种模式：NEVER，EMBEDDED（默认），Always

疑问：用EMBEDDED模式返回false，开关关闭，不执行脚本，这是为啥呢？

用Always模式则每次启动spring boot重复执行脚本（创建表脚本都是先判断有没有表，有则删除后重建）

``` java
private boolean isEnabled() {
  DataSourceInitializationMode mode = this.properties.getInitializationMode();
  if (mode == DataSourceInitializationMode.NEVER) {
    return false;
  }
  if (mode == DataSourceInitializationMode.EMBEDDED && !isEmbedded()) {
    return false;
  }
  return true;
}
```

7）通过配置文件指定需要执行脚本

```yaml
schema:
  - classpath:department.sql
```

创建出的 `department` 表

![img](.\images\spring-boot-05-data-jdbc\7.png)



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

