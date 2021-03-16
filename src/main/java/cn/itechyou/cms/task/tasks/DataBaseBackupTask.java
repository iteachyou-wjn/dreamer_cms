package cn.itechyou.cms.task.tasks;

import org.springframework.stereotype.Component;

import cn.itechyou.cms.task.ScheduledOfTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataBaseBackupTask implements ScheduledOfTask {

	@Override
	public void execute() {
		System.out.println("1211");
	}

}
