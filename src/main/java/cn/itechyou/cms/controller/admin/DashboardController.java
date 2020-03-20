package cn.itechyou.cms.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.DashboardService;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private ArchivesService archivesService;
	
	@RequestMapping("toIndex")
	public ModelAndView jump() {
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		ModelAndView mv = new ModelAndView();
		Map<String, Integer> statistics = dashboardService.statistics();
		String dbVersion = dashboardService.selectDBVersion();
		Map<String, String> serverInfo = new HashMap<String, String>();
		serverInfo.put("jvmName", props.getProperty("java.vm.name"));
		serverInfo.put("jreVersion", props.getProperty("java.version"));
		serverInfo.put("javaPath", props.getProperty("java.home"));
		serverInfo.put("jvmTotalMemory", String.valueOf(r.totalMemory()));
		serverInfo.put("jvmFreeMemory", String.valueOf(r.freeMemory()));
		serverInfo.put("optName", props.getProperty("os.name"));
		serverInfo.put("optFrameWork", props.getProperty("os.arch"));
		serverInfo.put("dbVersion", dbVersion);
		
		List<Archives> list = archivesService.queryListByTop();
		list.stream().forEach(item -> {
			if(item.getTitle().length() > 20) {
				item.setTitle(item.getTitle().substring(0, 20)+"...");
			}
		});
		
		mv.addObject("newest", list);
		mv.addObject("statistics", statistics);
		mv.addObject("serverInfo", serverInfo);
		mv.setViewName("admin/dashboard/index");
		return mv;
	}
	
	
	
}
