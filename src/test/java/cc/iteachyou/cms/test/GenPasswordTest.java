package cc.iteachyou.cms.test;

import java.io.IOException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class GenPasswordTest {

	@Test
	public void genUsernameAndPassword() throws IOException {
		String username = "admin";
		String password = "admin";
		
		ByteSource salt = ByteSource.Util.bytes(username + password);
		SimpleHash sh = new SimpleHash("MD5", password, salt, 1024);
        
        System.out.printf("密码：%s",sh.toString());
        System.out.println();
        System.out.printf("盐：%s",salt.toString());
	}
}
