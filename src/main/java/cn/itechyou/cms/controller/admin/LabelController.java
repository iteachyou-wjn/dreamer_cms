package cn.itechyou.cms.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.LabelService;

/**
 * 标签管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("/admin/label")
public class LabelController extends BaseController{
	
	@Autowired
	private LabelService labelService;
	@Autowired
	private ArchivesService archivesService;
	
	@RequestMapping("/toIndex")
	public String toIndex(Model model) {
		String[] classs = {"label-default","label-primary","label-success","label-info","label-inverse","label-warning","label-danger"};
		Map<String,List<Map<String,String>>> map = labelService.queryLabel();
		model.addAttribute("labels", map);
		model.addAttribute("classs", classs);
		return "admin/label/list";
	}
	
	@RequestMapping("/delete")
	public String delete(String tagName) {
		List<Archives> list = archivesService.queryListByTagName(tagName);
		if(list != null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				StringBuffer sb = new StringBuffer();
				Archives article = list.get(i);
				String tag = article.getTag();
				String[] tags = tag == null ? null : tag.split(",");
				if(tags != null && tags.length > 0) {
					for(int j = 0;j < tags.length;j++) {
						String temp = tags[j];
						if(!tagName.equals(temp)) {
							sb.append(temp + ",");
						}
					}
					String newTag = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
					article.setTag(newTag);
					archivesService.updateTagById(article);
				}
			}
		}
		labelService.deleteByTagName(tagName);
		return "redirect:/admin/label/toIndex";
	}
	
}
