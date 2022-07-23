package cc.iteachyou.cms.task.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import cc.iteachyou.cms.dao.ScheduledMapper;
import cc.iteachyou.cms.entity.Scheduled;
import cc.iteachyou.cms.task.ScheduledOfTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ScheduledMapper scheduledMapper;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Scheduled scheduled : scheduledMapper.selectAll()) {
            Class<?> clazz;
            Object task;
            try {
                clazz = Class.forName(scheduled.getClazzName());
                task = context.getBean(clazz);
            } catch (ClassNotFoundException e) {
            	log.error("system_scheduled表数据" + scheduled.getClazzName() + "不正确！");
                throw new IllegalArgumentException("system_scheduled表数据" + scheduled.getClazzName() + "不正确！", e);
            } catch (BeansException e) {
            	log.error(scheduled.getClazzName() + "未纳入到spring管理");
                throw new IllegalArgumentException(scheduled.getClazzName() + "未纳入到spring管理", e);
            }
            Assert.isAssignable(ScheduledOfTask.class, task.getClass(), "定时任务类必须实现ScheduledOfTask接口");
            // 可以通过改变数据库数据进而实现动态改变执行周期
            taskRegistrar.addTriggerTask(((Runnable) task),
                triggerContext -> {
                    String cronExpression = scheduled.getCronExpression();
                    return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);
                }
            );
        }
    }
    
    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}