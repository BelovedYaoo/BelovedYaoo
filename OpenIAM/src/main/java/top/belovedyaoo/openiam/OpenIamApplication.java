package top.belovedyaoo.openiam;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@EnableAutoTable(basePackages = "top.belovedyaoo")
@SpringBootApplication(scanBasePackages = "top.belovedyaoo")
@MapperScan(basePackages = "top.belovedyaoo")
public class OpenIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenIamApplication.class, args);
    }

}
