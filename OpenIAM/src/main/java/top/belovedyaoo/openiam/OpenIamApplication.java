package top.belovedyaoo.openiam;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类<br>
 * 因AGCore中存在系统关键的模块，如基础ID处理器，所以需要将基础模块的包名加入扫描路径中<br>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@EnableAutoTable(basePackages = {"top.belovedyaoo.openiam", "top.belovedyaoo.logs"})
@SpringBootApplication(scanBasePackages = {"top.belovedyaoo.openiam", "top.belovedyaoo.agcore", "top.belovedyaoo.logs", "top.belovedyaoo.weaver"})
@MapperScan(basePackages = {"top.belovedyaoo.openiam", "top.belovedyaoo.logs"})
public class OpenIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenIamApplication.class, args);
    }

}
