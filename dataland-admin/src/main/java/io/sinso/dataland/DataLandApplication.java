package io.sinso.dataland;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author sum
 * @version 1.0.0
 * @date 2021/2/19
 */
@SpringBootApplication
@MapperScan(basePackages = "io.sinso.dataland.mapper")
public class DataLandApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DataLandApplication.class);
    }
}
