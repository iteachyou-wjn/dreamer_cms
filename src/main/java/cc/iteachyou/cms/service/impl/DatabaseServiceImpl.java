package cc.iteachyou.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.iteachyou.cms.dao.DatabaseMapper;
import cc.iteachyou.cms.dao.SystemMapper;
import cc.iteachyou.cms.service.DatabaseService;
import cc.iteachyou.cms.thread.BackupThread;
import cc.iteachyou.cms.thread.RestoreThread;
import cc.iteachyou.cms.utils.FileConfiguration;

@Service
public class DatabaseServiceImpl implements DatabaseService {
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private DatabaseMapper databaseMapper;
	@Autowired
	private DataSource dataSource;

	@Override
	public List<String> showTables() {
		return databaseMapper.showTables();
	}

	@Override
	public int backup(String tableName) {
		BackupThread backupThread = new BackupThread();
		backupThread.setFileConfiguration(fileConfiguration);
		backupThread.setSystemMapper(systemMapper);
		backupThread.setDatabaseMapper(databaseMapper);
		backupThread.setTableNames(new String[] {tableName});
		Thread t = new Thread(backupThread);
		t.start();
		return 1;
	}

	@Override
	public String showStruct(String tableName) {
		Map<String, String> struct = databaseMapper.showTableStruct(tableName);
		return struct.get("Create Table");
	}

	@Override
	public int restore(String[] fileNames) {
		RestoreThread restoreThread = new RestoreThread();
		restoreThread.setFileConfiguration(fileConfiguration);
		restoreThread.setSystemMapper(systemMapper);
		restoreThread.setDataSource(dataSource);
		restoreThread.setFiles(fileNames);
		Thread t = new Thread(restoreThread);
		t.start();
		return 1;
	}
	
	
}
