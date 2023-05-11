package cc.iteachyou.cms.controller.admin;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Scheduled;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.ScheduledService;
import cc.iteachyou.cms.task.ScheduledOfTask;
import cc.iteachyou.cms.utils.CronUtils;
import cn.hutool.core.util.IdUtil;

/**
 * 计划任务(需要做权限控制)
 * @author 王俊南
 * @date 2021/3/16
 */
@Controller
@RequestMapping("admin/task")
public class TaskController extends BaseController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ScheduledService scheduledService;

    /**
     * 查看任务列表
     */
    @Log(operType = OperatorType.PAGE, module = "计划任务", content = "计划任务分页列表")
    @RequestMapping({"","/list"})
    @RequiresPermissions("system:task:page")
    public String taskList(Model model, SearchEntity params) {
        PageInfo<Scheduled> page = scheduledService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/task/list";
    }

    /**
	 * 添加跳转
	 */
    @Log(operType = OperatorType.OTHER, module = "计划任务", content = "添加计划任务页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("system:task:toadd")
	public String toAdd(Model model) {
		return "admin/task/add";
	}
	
	/**
     * 编辑任务cron表达式
     */
    @Log(operType = OperatorType.INSERT, module = "计划任务", content = "添加计划任务")
    @RequestMapping("/add")
    @RequiresPermissions("system:task:add")
    public String add(Scheduled scheduled) {
        if (!CronUtils.isValidExpression(scheduled.getCronExpression())) {
            throw new IllegalArgumentException("失败,非法表达式:" + scheduled.getCronExpression());
        }
        scheduled.setId(IdUtil.getSnowflakeNextIdStr());
        scheduled.setCreateBy(TokenManager.getUserId());
        scheduled.setCreateTime(new Date());
        int i = scheduledService.add(scheduled);
        return "redirect:/admin/task/list";
    }
	
	/**
	 * 编辑跳转
	 */
    @Log(operType = OperatorType.OTHER, module = "计划任务", content = "修改计划任务页面")
	@RequestMapping("/toEdit")
	@RequiresPermissions("system:task:toedit")
	public String toEdit(Model model,String id) {
		Scheduled scheduled = scheduledService.queryScheduledById(id);
		model.addAttribute("scheduled", scheduled);
		return "admin/task/edit";
	}
    
    /**
     * 编辑任务
     */
    @Log(operType = OperatorType.UPDATE, module = "计划任务", content = "修改计划任务")
    @RequestMapping("/update")
    @RequiresPermissions("system:task:update")
    public String update(Scheduled scheduled) {
        if (!CronUtils.isValidExpression(scheduled.getCronExpression())) {
            throw new IllegalArgumentException("失败,非法表达式:" + scheduled.getCronExpression());
        }
        scheduled.setUpdateBy(TokenManager.getUserId());
        scheduled.setUpdateTime(new Date());
        int i = scheduledService.update(scheduled);
        return "redirect:/admin/task/list";
    }

    /**
	 * 删除
	 */
    @Log(operType = OperatorType.DELETE, module = "计划任务", content = "修改计划任务")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("system:task:delete")
	public String delete(Model model, String id) {
		scheduledService.delete(id);
		return "redirect:/admin/task/list";
	}
    
    /**
     * 执行定时任务
     */
    @Log(operType = OperatorType.RUN, module = "计划任务", content = "执行计划任务")
    @RequestMapping("/run")
    @RequiresPermissions("system:task:run")
    @ResponseBody
    public ResponseResult runTaskCron(String id) throws Exception {
    	Scheduled scheduled = scheduledService.queryScheduledById(id);
        ((ScheduledOfTask) context.getBean(Class.forName(scheduled.getClazzName()))).execute();
        return ResponseResult.Factory.newInstance(Boolean.TRUE,
				StateCodeEnum.HTTP_SUCCESS.getCode(), null,
				StateCodeEnum.HTTP_SUCCESS.getDescription());
    }

    /**
     * 启用或禁用定时任务
     */
    @Log(operType = OperatorType.UPDATE, module = "计划任务", content = "修改计划任务状态")
    @RequestMapping("/changeStatus")
    @RequiresPermissions("system:task:status")
    public String changeStatusTaskCron(String id, String status) {
    	Scheduled scheduled = new Scheduled();
    	scheduled.setId(id);
    	scheduled.setStatus(status);
    	int i = scheduledService.update(scheduled);
    	return "redirect:/admin/task/list";
    }

}
