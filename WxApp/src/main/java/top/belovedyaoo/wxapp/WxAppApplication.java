package top.belovedyaoo.wxapp;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoTable(basePackages = {"top.belovedyaoo"})
@SpringBootApplication(scanBasePackages =  {"top.belovedyaoo"})
@MapperScan(basePackages = {"top.belovedyaoo"})
public class WxAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxAppApplication.class, args);
    }

}
