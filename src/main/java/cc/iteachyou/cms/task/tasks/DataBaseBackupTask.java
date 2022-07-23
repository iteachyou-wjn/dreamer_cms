package cc.iteachyou.cms.task.tasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.dao.DatabaseMapper;
import cc.iteachyou.cms.dao.SystemMapper;
import cc.iteachyou.cms.task.ScheduledOfTask;
import cc.iteachyou.cms.thread.BackupThread;
import cc.iteachyou.cms.utils.FileConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataBaseBackupTask implements ScheduledOfTask {
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private DatabaseMapper databaseMapper;

	@Override
	public void execute() {
		BackupThread backupThread = new BackupThread();
		backupThread.setFileConfiguration(fileConfiguration);
		backupThread.setSystemMapper(systemMapper);
		backupThread.setDatabaseMapper(databaseMapper);
		
		List<String> tables = databaseMapper.showTables();
		
		String[] tableArr = new String[tables.size()];
		tables.toArray(tableArr);
		
		backupThread.setTableNames(tableArr);
		Thread t = new Thread(backupThread);
		t.start();
	}

}
