package cn.itechyou.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement //开启注解事务管理,等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@MapperScan("cn.itechyou.cms.dao")
public class DreamerCMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamerCMSApplication.class, args);
	}
	
}
