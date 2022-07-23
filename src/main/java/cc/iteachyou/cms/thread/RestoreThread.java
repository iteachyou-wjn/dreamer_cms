package cc.iteachyou.cms.thread;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;

import cc.iteachyou.cms.dao.SystemMapper;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.utils.FileConfiguration;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class RestoreThread implements Runnable {
	private DataSource dataSource;
	private FileConfiguration fileConfiguration;
	private SystemMapper systemMapper;
	private String[] files;
	
	@Override
	public void run() {
		System system = systemMapper.getCurrentSystem();
		String resourceDir = fileConfiguration.getResourceDir();
		
		log.info("准备执行数据库还原操作...");
		Connection connection;
		try {
			connection = dataSource.getConnection();
			ScriptRunner runner = new ScriptRunner(connection);
			runner.setStopOnError(true);
			
			//循环执行数据库还原操作
			for(int i = 0;i < files.length;i++) {
				String filePath = files[i];
				
				filePath = resourceDir + system.getUploaddir() + filePath;
				java.lang.System.out.println(filePath);
				runner.runScript(new FileReader(new File(filePath)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("执行数据库还原操作失败...");
			log.error(e.getMessage());
		}
		log.info("执行数据库还原操作成功...");
	}

}
