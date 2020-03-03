package com.jackson0714.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    // 配置Durid监控
    // 1、配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> servletInitParams = new HashMap<>();
        servletInitParams.put("loginUsername", "Admin");
        servletInitParams.put("loginPassword", "abc123");
        //servletInitParams.put("allow",""); // 默认允许所有
        servletInitParams.put("deny","192.168.10.160"); // 拒绝访问
        servletRegistrationBean.setInitParameters(servletInitParams);

        return servletRegistrationBean;
    }

    // 2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());

        Map<String, String> filterInitParams = new HashMap<>();
        filterInitParams.put("exclusions", "*.js,*.css,/druid/*");// 不拦截js、css文件请求，不拦截/druid/*的请求
        filterRegistrationBean.setInitParameters(filterInitParams);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*")); // 拦截所有请求

        return filterRegistrationBean;
    }
}
