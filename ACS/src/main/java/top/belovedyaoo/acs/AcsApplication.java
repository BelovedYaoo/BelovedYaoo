package top.belovedyaoo.acs;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ACS 启动类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@EnableScheduling
@MapperScan(basePackages = {"top.belovedyaoo.acs", "top.belovedyaoo.opencore"})
@EnableAutoTable(basePackages = {"top.belovedyaoo.acs", "top.belovedyaoo.agcore"})
@SpringBootApplication(scanBasePackages =  {"top.belovedyaoo.acs", "top.belovedyaoo.opencore"})
public class AcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcsApplication.class, args);
	}

}
