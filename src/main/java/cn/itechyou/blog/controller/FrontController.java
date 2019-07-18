package cn.itechyou.blog.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Archives;
import cn.itechyou.blog.entity.ArchivesWithRownum;
import cn.itechyou.blog.entity.Category;
import cn.itechyou.blog.entity.CategoryWithBLOBs;
import cn.itechyou.blog.entity.Field;
import cn.itechyou.blog.entity.Form;
import cn.itechyou.blog.entity.SearchRecord;
import cn.itechyou.blog.entity.Theme;
import cn.itechyou.blog.service.ArchivesService;
import cn.itechyou.blog.service.CategoryService;
import cn.itechyou.blog.service.FieldService;
import cn.itechyou.blog.service.FormService;
import cn.itechyou.blog.service.PagesService;
import cn.itechyou.blog.service.SearchRecordService;
import cn.itechyou.blog.service.ThemeService;
import cn.itechyou.blog.utils.StringUtils;
import cn.itechyou.blog.utils.UUIDUtils;
import cn.itechyou.blog.vo.ArchivesVo;

@Controller
@RequestMapping("/")
public class FrontController {
	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ThemeService themeService;
	@Autowired
	private PagesService pagesService;
	@Autowired
	private FormService formService;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private SearchRecordService searchRecordService;
	
	@RequestMapping("/index")
	public String router(Model model) {
		Theme theme = themeService.getCurrentTheme();
		StringBuffer templatePath = new StringBuffer();
		model.addAttribute("theme", theme);
		templatePath.append("/themes/" + theme.getThemePath());
		templatePath.append("/index.html");
		return templatePath.toString();
	}
	
	@RequestMapping("{path}")
	public String router(
			HttpServletRequest request,HttpServletResponse response,
			Model model, @PathVariable String path, 
			String typeid, String pageNum,String pageSize) {
		Theme theme = themeService.getCurrentTheme();
		if(!path.startsWith("/")) {
			path = "/" + path;
		}
		CategoryWithBLOBs category = categoryService.selectById(typeid);
		
		StringBuffer templatePath = new StringBuffer();
		if(theme == null) {
			
		}
		model.addAttribute("theme", theme);
		templatePath.append("/themes/" + theme.getThemePath());
		
		if(category.getCatModel() == 1) {
			templatePath.append(category.getCoverTemp());
		}else if(category.getCatModel() == 2) {
			templatePath.append(category.getListTemp());
		}else if(category.getCatModel() == 3) {
			templatePath.append(category.getLinkUrl());
		}
		
		//分页信息
		if(StringUtils.isBlank(pageNum)) {
			pageNum = "0";
		}
		if(StringUtils.isBlank(pageSize)) {
			pageSize = "10";
		}
		Map<String,Object> pageinfo = new HashMap<String,Object>();
		pageinfo.put("pageNum", pageNum);
		pageinfo.put("pageSize", pageSize);
		model.addAttribute("pageinfo", pageinfo);
		
		//当前url
		String requestURI = request.getRequestURI();
		model.addAttribute("currentURI", requestURI);
		
		model.addAttribute("category", category);
		return templatePath.toString();
	}
	
	@RequestMapping("/article/{id}")
	public String article(Model model, @PathVariable String id) {
		StringBuffer templatePath = new StringBuffer();
		Theme theme = themeService.getCurrentTheme();
		if(theme == null) {
			
		}
		model.addAttribute("theme", theme);
		templatePath.append("/themes/" + theme.getThemePath());

		Archives archives = archivesService.selectByPrimaryKey(id);
		String formId = formService.queryDefaultForm().getId();
		Category category = null;
		if(!"-1".equals(archives.getCategoryId())) {
			category = categoryService.selectById(archives.getCategoryId());
			formId = category.getFormId();
			//构建路径
			templatePath.append("/" + category.getArticleTemp());
			model.addAttribute("category", category);
		}else {//顶级分类走该模版
			templatePath.append("/article.html");
			category = new Category();
			category.setId("-1");
			category.setCnname("顶级分类");
			model.addAttribute("category", category);
		}
		
		Form form = formService.queryFormById(formId);
		List<Field> fields = fieldService.queryFieldByFormId(formId);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("tableName", "system_" + form.getTableName());
		params.put("id", archives.getId());
		
		Map<String, Object> article = archivesService.queryArticleById(params);
		model.addAttribute("article", article);
		
		//上一篇下一篇
		params = new HashMap<String, Object>();
		params.put("arcid", article.get("aid").toString());
		params.put("categoryId", category.getId());
		ArchivesWithRownum currentArticle = archivesService.queryArticleRowNum(params);
		
		params.remove("arcid");
		params.put("privNum", (currentArticle.getRownum() - 1)+"");
		ArchivesWithRownum prevArc = archivesService.queryArticleRowNum(params);
		
		params.remove("privNum");
		params.put("nextNum", (currentArticle.getRownum() + 1)+"");
		ArchivesWithRownum nextArc = archivesService.queryArticleRowNum(params);
		if(prevArc == null) {
			prevArc = new ArchivesWithRownum();
			prevArc.setTitle("没有了");
		}
		if(nextArc == null) {
			nextArc = new ArchivesWithRownum();
			nextArc.setTitle("没有了");
		}
		Map<String,Object> privAndNext = new HashMap<String,Object>();
		privAndNext.put("previous", prevArc);
		privAndNext.put("next", nextArc);
		model.addAttribute("prevnext", privAndNext);
		
		return templatePath.toString();
	}
	
	@RequestMapping(value = "/search")
	public String search(Model model, SearchEntity params) {
		StringBuffer templatePath = new StringBuffer();
		Theme theme = themeService.getCurrentTheme();
		if(theme == null) {
			
		}
		model.addAttribute("theme", theme);
		
		PageInfo<ArchivesVo> pageinfo = archivesService.queryListByKeywords(params);
		model.addAttribute("result", pageinfo);
		
		String keywords = params.getEntity().get("keywords").toString();
		
		//记录搜索关键词
		SearchRecord sr = new SearchRecord();
		sr.setId(UUIDUtils.getPrimaryKey());
		sr.setKeywords(keywords);
		sr.setCreateTime(new Date());
		searchRecordService.add(sr);
		
		model.addAttribute("keywords", keywords);
		templatePath.append("/themes/" + theme.getThemePath() + "/search.html");
		return templatePath.toString();
	}
}
