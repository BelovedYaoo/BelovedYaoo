package top.belovedyaoo.acs;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = {"top.belovedyaoo.acs", "top.belovedyaoo.agcore"})
@EnableAutoTable(basePackages = {"top.belovedyaoo.acs", "top.belovedyaoo.agcore"})
@SpringBootApplication(scanBasePackages =  {"top.belovedyaoo.acs", "top.belovedyaoo.agcore"})
public class AcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcsApplication.class, args);
	}

}
