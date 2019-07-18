package cn.itechyou.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement //开启注解事务管理,等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@MapperScan("cn.itechyou.blog.dao")
public class DreamerBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamerBlogApplication.class, args);
	}
	
}
