package top.belovedyaoo.openauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = {"top.belovedyaoo.openauth", "top.belovedyaoo.agcore","com.tangzc.mybatisflex.annotation.handler"})
public class OpenAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenAuthApplication.class, args);
    }

}
