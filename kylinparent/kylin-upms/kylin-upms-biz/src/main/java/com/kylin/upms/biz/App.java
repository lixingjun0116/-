package com.kylin.upms.biz;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication(exclude={RedisAutoConfiguration.class})
@SpringBootApplication
@MapperScan(basePackages={"com.kylin.upms.biz.mapper"})
@EnableSwagger2
@EnableDiscoveryClient

public class App {
    public static void main(String[] args) {

        SpringApplication.run(App.class,args);
    }

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient("http://192.168.74.135:9000","admin","admin123456");
    }
}
