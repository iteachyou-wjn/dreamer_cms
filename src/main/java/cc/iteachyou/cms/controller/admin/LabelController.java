package cc.iteachyou.cms.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.service.LabelService;

/**
 * 标签管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/label")
public class LabelController extends BaseController{
	
	@Autowired
	private LabelService labelService;
	@Autowired
	private ArchivesService archivesService;
	
	@Log(operType = OperatorType.SELECT, module = "标签管理", content = "标签分组查询")
	@RequestMapping({"","/toIndex"})
	public String toIndex(Model model) {
		String[] classs = {"label-default","label-primary","label-success","label-info","label-inverse","label-warning","label-danger"};
		Map<String,List<Map<String,String>>> map = labelService.queryLabel();
		model.addAttribute("labels", map);
		model.addAttribute("classs", classs);
		return "admin/label/list";
	}
	
	@Log(operType = OperatorType.DELETE, module = "标签管理", content = "删除标签")
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
