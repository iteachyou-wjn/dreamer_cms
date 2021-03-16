package cn.itechyou.cms.task;


import cn.itechyou.cms.dao.ScheduledMapper;
import cn.itechyou.cms.entity.Scheduled;
import cn.itechyou.cms.utils.SpringContextUtil;

/**
 * @author 王俊南
 * @date 2021/3/16
 */
public interface ScheduledOfTask extends Runnable {

    /**
     * 定时任务方法
     */
    void execute();

    /**
     * 实现控制定时任务启用或禁用的功能
     */
    @Override
    default void run() {
    	ScheduledMapper scheduledMapper = SpringContextUtil.getBean(ScheduledMapper.class);
        Scheduled scheduled = scheduledMapper.selectOneByClassName(this.getClass().getName());
        if ("0".equals(scheduled.getStatus())) {
            // 任务是禁用状态
            return;
        }
        execute();
    }
}
