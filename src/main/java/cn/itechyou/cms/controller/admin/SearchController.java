package cn.itechyou.cms.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.vo.ArchivesVo;

@Controller
@RequestMapping("admin/search")
public class SearchController {
	@Autowired
	private ArchivesService archivesService;
	
	@RequestMapping("/doSearch")
	public String doSearch(Model model ,SearchEntity params) {
		if(params.getEntity() == null) {
			Map<String,Object> entity = new HashMap<String,Object>();
			params.setEntity(entity);
		}
		PageInfo<ArchivesVo> archives = archivesService.queryListByKeywords(params);
		model.addAttribute("keywords", params.getEntity().containsKey("keywords") ? params.getEntity().get("keywords") : "");
		model.addAttribute("archives", archives);
		return "admin/search/result";
	}

}
