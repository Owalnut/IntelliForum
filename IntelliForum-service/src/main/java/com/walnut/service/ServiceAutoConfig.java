package com.walnut.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Configuration
@ComponentScan("com.walnut.service")
@MapperScan(basePackages = {
        "com.walnut.service.article.repository.mapper",
        "com.walnut.service.user.repository.mapper",
        "com.walnut.service.comment.repository.mapper",
        "com.walnut.service.config.repository.mapper",
        "com.walnut.service.statistics.repository.mapper",
        "com.walnut.service.notify.repository.mapper",})
public class ServiceAutoConfig {

}

