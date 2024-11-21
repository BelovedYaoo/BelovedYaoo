package top.belovedyaoo.acs;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = {"top.belovedyaoo"})
@EnableAutoTable(basePackages = {"top.belovedyaoo"})
@SpringBootApplication(scanBasePackages =  {"top.belovedyaoo"})
public class AcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcsApplication.class, args);
	}

}
