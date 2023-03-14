package cc.iteachyou.cms.utils;

import java.util.Date;

import org.quartz.impl.triggers.CronTriggerImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yudong
 * @date 2019/5/10
 */
@Slf4j
public class CronUtils {
    public static boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (Exception e) {
            log.error("invalid expression:{},error msg:{}", cronExpression, e.getMessage());
        }
        return false;
    }

}
