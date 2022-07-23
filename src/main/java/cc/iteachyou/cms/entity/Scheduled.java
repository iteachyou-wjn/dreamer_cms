package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "system_scheduled")
@Data
public class Scheduled implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 完整类路径
     */
    @Column(name = "clazz_name")
    private String clazzName;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 状态（1：正常；0：停用）
     */
    @Column(name = "status")
    private String status;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;

    private static final long serialVersionUID = 1L;
}