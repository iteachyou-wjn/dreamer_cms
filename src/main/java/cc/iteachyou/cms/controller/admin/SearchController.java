package cc.iteachyou.cms.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.vo.ArchivesVo;

/**
 * 搜索
 * @author 王俊南
 *
 */
@Controller
@RequestMapping("admin/search")
public class SearchController {
	@Autowired
	private ArchivesService archivesService;
	
	@Log(operType = OperatorType.SELECT, module = "搜索模块", content = "全局搜索")
	@RequestMapping("/doSearch")
	public String doSearch(Model model ,SearchEntity params) {
		if(params.getEntity() == null) {
			Map<String,Object> entity = new HashMap<String,Object>();
			params.setEntity(entity);
		}
		PageInfo<ArchivesVo> page = archivesService.queryListByKeywords(params);
		model.addAttribute("keywords", params.getEntity().containsKey("keywords") ? params.getEntity().get("keywords") : "");
		model.addAttribute("page", page);
		return "admin/search/result";
	}

}
