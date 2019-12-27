package cn.itechyou.cms.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 * @author Wangjn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String content();//日志内容
    String module();//功能模块名
    Level level() default Level.INFO;//日志级别
    Target target() default Target.ALL;//日志记录到哪

    enum Level{
        DEBUG, INFO, ERROR
    }

    enum Target{
        ALL, DB, LOGGER
    }
}
