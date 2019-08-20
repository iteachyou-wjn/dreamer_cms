package cn.itechyou.blog.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.Constant;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Archives;
import cn.itechyou.blog.entity.ArchivesWithRownum;
import cn.itechyou.blog.entity.Category;
import cn.itechyou.blog.entity.CategoryWithBLOBs;
import cn.itechyou.blog.entity.Field;
import cn.itechyou.blog.entity.Form;
import cn.itechyou.blog.entity.SearchRecord;
import cn.itechyou.blog.entity.Theme;
import cn.itechyou.blog.exception.TemplateNotFoundException;
import cn.itechyou.blog.service.ArchivesService;
import cn.itechyou.blog.service.CategoryService;
import cn.itechyou.blog.service.FieldService;
import cn.itechyou.blog.service.FormService;
import cn.itechyou.blog.service.PagesService;
import cn.itechyou.blog.service.SearchRecordService;
import cn.itechyou.blog.service.ThemeService;
import cn.itechyou.blog.taglib.ParseEngine;
import cn.itechyou.blog.utils.FileConfiguration;
import cn.itechyou.blog.utils.StringUtil;
import cn.itechyou.blog.utils.UUIDUtils;
import cn.itechyou.blog.vo.ArchivesVo;

@Controller
@RequestMapping("/")
public class FrontController {
	
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session;

    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    } 
    
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
	@Autowired
	private FileConfiguration fileConfiguration;
	
	@Autowired
	private ParseEngine parseEngine;
	
	/**
	 * 首页方法
	 * @param model
	 * @param request
	 * @param response
	 * @throws TemplateNotFoundException
	 * @throws IOException
	 */
	@RequestMapping("/index")
	public void index(Model model
			,HttpServletRequest request
			,HttpServletResponse response) throws TemplateNotFoundException, IOException {
		Theme theme = themeService.getCurrentTheme();
		String templatePath = theme.getThemePath() + "/index.html";
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		
		String path = templateDir + templatePath;
		File template = new File(path);
		if(!template.exists()) {
			throw new TemplateNotFoundException("");
		}
		String newHtml = "";
		try {
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			outHtml(newHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 封面方法
	 * @param model
	 * @param typeid
	 * @param visitUrl
	 * @throws TemplateNotFoundException
	 * @throws IOException
	 */
	@RequestMapping("cover-{typeid}/{visitUrl}")
	public void cover(Model model
			, @PathVariable String typeid
			, @PathVariable String visitUrl) throws TemplateNotFoundException, IOException {
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		CategoryWithBLOBs category = categoryService.queryCategoryByCode(typeid);
		StringBuffer templatePath = new StringBuffer();
		templatePath.append(theme.getThemePath());
		
		if(category.getCatModel() == 1) {
			templatePath.append(category.getCoverTemp());
		}else if(category.getCatModel() == 2) {
			templatePath.append(category.getListTemp());
		}else if(category.getCatModel() == 3) {
			templatePath.append(category.getLinkUrl());
		}
		
		try {
			String path = templateDir + templatePath;
			File template = new File(path);
			if(!template.exists()) {
				throw new TemplateNotFoundException("");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml,typeid);
			outHtml(newHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 列表方法
	 * @param model
	 * @param visitUrl
	 * @param typeid
	 * @param pageNum
	 * @throws TemplateNotFoundException
	 * @throws IOException
	 */
	@RequestMapping("list-{typeid}/{visitUrl}/{pageNum}/{pageSize}")
	public void list(Model model
			, @PathVariable String typeid
			, @PathVariable String visitUrl
			, @PathVariable Integer pageNum
			, @PathVariable Integer pageSize) throws TemplateNotFoundException, IOException {
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		CategoryWithBLOBs category = categoryService.queryCategoryByCode(typeid);
		StringBuffer templatePath = new StringBuffer();
		templatePath.append(theme.getThemePath());
		
		if(category.getCatModel() == 1) {
			templatePath.append(category.getCoverTemp());
		}else if(category.getCatModel() == 2) {
			templatePath.append(category.getListTemp());
		}else if(category.getCatModel() == 3) {
			templatePath.append(category.getLinkUrl());
		}
		try {
			String path = templateDir + templatePath;
			File template = new File(path);
			if(!template.exists()) {
				throw new TemplateNotFoundException("");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml,typeid);
			newHtml = parseEngine.parsePageList(newHtml, typeid, pageNum, pageSize);
			outHtml(newHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/article/{id}")
	public void article(Model model
			, @PathVariable String id) throws TemplateNotFoundException, IOException{
		StringBuffer templatePath = new StringBuffer();
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		templatePath.append(theme.getThemePath());

		Archives archives = archivesService.selectByPrimaryKey(id);
		String formId = formService.queryDefaultForm().getId();
		Category category = null;
		if(!"-1".equals(archives.getCategoryId())) {
			category = categoryService.selectById(archives.getCategoryId());
			formId = category.getFormId();
			//构建路径
			templatePath.append("/" + category.getArticleTemp());
		}else {//顶级分类走该模版
			templatePath.append("/article.html");
			category = new Category();
			category.setId("-1");
			category.setCnname("顶级分类");
		}
		try {
			String path = templateDir + templatePath;
			File template = new File(path);
			if(!template.exists()) {
				throw new TemplateNotFoundException("");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml, category.getCode());
			newHtml = parseEngine.parseArticle(newHtml, id);
			outHtml(newHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * //上一篇下一篇 params = new HashMap<String, Object>(); params.put("arcid",
		 * article.get("aid").toString()); params.put("categoryId", category.getId());
		 * ArchivesWithRownum currentArticle =
		 * archivesService.queryArticleRowNum(params);
		 * 
		 * params.remove("arcid"); params.put("privNum", (currentArticle.getRownum() -
		 * 1)+""); ArchivesWithRownum prevArc =
		 * archivesService.queryArticleRowNum(params);
		 * 
		 * params.remove("privNum"); params.put("nextNum", (currentArticle.getRownum() +
		 * 1)+""); ArchivesWithRownum nextArc =
		 * archivesService.queryArticleRowNum(params); if(prevArc == null) { prevArc =
		 * new ArchivesWithRownum(); prevArc.setTitle("没有了"); } if(nextArc == null) {
		 * nextArc = new ArchivesWithRownum(); nextArc.setTitle("没有了"); }
		 */
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
	
	/**
	 * 取得HttpServletRequest对象
	 * @return HttpServletRequest对象
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 取得Response对象
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	
	/**
	 * 输出字符串到页面
	 * @param str 字符
	 */
	public void outHtml(String html) {
		try {
			HttpServletResponse httpServletResponse = getResponse();
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setContentType("text/html;charset=utf-8");
			httpServletResponse.setHeader("Cache-Control", "no-cache");
			httpServletResponse.setHeader("Cache-Control", "no-store");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0L);
			httpServletResponse.getWriter().write(html);
			httpServletResponse.flushBuffer();
		} catch (IOException e) {
			
		} 
	}
}
