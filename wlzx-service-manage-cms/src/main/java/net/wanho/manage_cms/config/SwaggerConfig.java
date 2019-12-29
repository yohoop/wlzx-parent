package net.wanho.manage_cms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// 扫描接口中的wagger描述
@ComponentScan("net.wanho.api.config")
public class SwaggerConfig {
}
