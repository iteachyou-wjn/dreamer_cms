package cc.iteachyou.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling //开启定时任务
@EnableTransactionManagement //开启注解事务管理
@MapperScan("cc.iteachyou.cms.dao")
public class DreamerCMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamerCMSApplication.class, args);
	}
	
}
