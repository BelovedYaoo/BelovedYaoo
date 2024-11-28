package top.belovedyaoo.openiam;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 启动类<br>
 * 因AGCore中存在系统关键的模块，如基础ID处理器，所以需要将基础模块的包名加入扫描路径中<br>
 * 因本项目包名与AGCore包名的一、二级包名都相同，故此处直接扫描二级包名
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@EnableAutoTable(basePackages = {"top.belovedyaoo"})
@SpringBootApplication(scanBasePackages = {"top.belovedyaoo"})
@MapperScan(basePackages = {"top.belovedyaoo"})
public class OpenIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenIamApplication.class, args);
    }

}
