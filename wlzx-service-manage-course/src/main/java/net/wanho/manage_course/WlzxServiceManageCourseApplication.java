package net.wanho.manage_course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan("net.wano.po.course")//扫描实体类
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
public class WlzxServiceManageCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceManageCourseApplication.class, args);
    }

}
