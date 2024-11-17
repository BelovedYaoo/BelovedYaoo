package top.belovedyaoo.acs;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoTable(basePackages = {"top.belovedyaoo"})
@SpringBootApplication(scanBasePackages =  {"top.belovedyaoo"})
@MapperScan(basePackages = {"top.belovedyaoo"})
public class AcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcsApplication.class, args);
	}

}
