package cc.iteachyou.cms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;

import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.Attachment;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.entity.SearchRecord;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.entity.Theme;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.FormParameterException;
import cc.iteachyou.cms.exception.TemplateNotFoundException;
import cc.iteachyou.cms.exception.TemplateReadException;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.service.AttachmentService;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.FieldService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.SearchRecordService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.service.ThemeService;
import cc.iteachyou.cms.taglib.ParseEngine;
import cc.iteachyou.cms.taglib.utils.URLUtils;
import cc.iteachyou.cms.utils.FileConfiguration;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;

@Controller
@Scope("prototype")
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
	private FormService formService;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private SearchRecordService searchRecordService;
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ParseEngine parseEngine;
	
	/**
	 * 首页方法
	 * @throws CmsException
	 * @throws IOException 
	 */
	@RequestMapping("/index")
	public void index() throws CmsException, IOException {
		System system = systemService.getSystem();
		String staticdir = system.getStaticdir();
		if(staticdir.startsWith("/")) {
			staticdir = staticdir.substring(1);
		}
		//如果为静态浏览，则重写向到静态文件
		if(2 == system.getBrowseType()) {
			String url = fileConfiguration.getResourceDir() + staticdir + "/index.html";
			File staticFile = new File(url);
			if(!staticFile.exists()) {
				throw new TemplateNotFoundException(
						ExceptionEnum.HTTP_NOT_FOUND.getCode(),
						ExceptionEnum.HTTP_NOT_FOUND.getMessage(),
						"当前浏览方式为静态浏览，您所浏览的静态文件不存在，请先静态化网站后再继续。");
			}
			response.sendRedirect("/" + staticdir + "/index.html");
			return;
		}
		
		Theme theme = themeService.getCurrentTheme();
		String templatePath = theme.getThemePath() + "/index.html";
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		
		String path = templateDir + templatePath;
		File template = new File(path);
		if(!template.exists()) {
			throw new TemplateNotFoundException(
					ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
					"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
		String newHtml = "";
		try {
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			outHtml(newHtml);
		} catch (IOException e) {
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 封面方法
	 * @param typeid 栏目编码
	 * @param visitUrl 访问URL
	 * @throws CmsException
	 * @throws IOException 
	 */
	@RequestMapping("cover-{typeid}/{visitUrl}")
	public void cover(@PathVariable String typeid
			, @PathVariable String visitUrl) throws CmsException, IOException {
		System system = systemService.getSystem();
		//查询栏目
		Category category = categoryService.queryCategoryByCode(typeid);
		//如果为静态浏览，则重写向到静态文件
		if(2 == system.getBrowseType()) {
			String url = URLUtils.parseURL(system, category, "S");
			String fileUrl = fileConfiguration.getResourceDir() + url;
			File staticFile = new File(fileUrl);
			if(!staticFile.exists()) {
				throw new TemplateNotFoundException(
						ExceptionEnum.HTTP_NOT_FOUND.getCode(),
						ExceptionEnum.HTTP_NOT_FOUND.getMessage(),
						"当前浏览方式为静态浏览，您所浏览的静态文件不存在，请先静态化网站后再继续。");
			}
			response.sendRedirect(url);
			return;
		}
		
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
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
				throw new TemplateNotFoundException(
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
						"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml,typeid);
			outHtml(newHtml);
		} catch (IOException e) {
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 列表方法
	 * @param typeid 栏目编码
	 * @param visitUrl 访问URL
	 * @param pageNum 当前页
	 * @param pageSize 分页大小
	 * @throws CmsException
	 * @throws IOException 
	 */
	@RequestMapping("list-{typeid}/{visitUrl}/{pageNum}/{pageSize}")
	public void list(@PathVariable String typeid
			, @PathVariable String visitUrl
			, @PathVariable Integer pageNum
			, @PathVariable Integer pageSize) throws CmsException, IOException {
		System system = systemService.getSystem();
		String staticdir = system.getStaticdir();
		if(staticdir.startsWith("/")) {
			staticdir = staticdir.substring(1);
		}
		//查询栏目
		Category category = categoryService.queryCategoryByCode(typeid);
		//如果为静态浏览，则重写向到静态文件
		if(2 == system.getBrowseType()) {
			String url = staticdir + URLUtils.parseFileName(category, pageNum);
			String fileUrl = fileConfiguration.getResourceDir() + url;
			File staticFile = new File(fileUrl);
			if(!staticFile.exists()) {
				throw new TemplateNotFoundException(
						ExceptionEnum.HTTP_NOT_FOUND.getCode(),
						ExceptionEnum.HTTP_NOT_FOUND.getMessage(),
						"当前浏览方式为静态浏览，您所浏览的静态文件不存在，请先静态化网站后再继续。");
			}
			response.sendRedirect("/" + url);
			return;
		}
		
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
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
				throw new TemplateNotFoundException(
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
						"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml, typeid);
			newHtml = parseEngine.parsePageList(newHtml, typeid, pageNum, pageSize);
			outHtml(newHtml);
		} catch (IOException e) {
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 文章详情方法
	 * @param id 文章ID
	 * @throws CmsException
	 * @throws IOException 
	 */
	@RequestMapping("/article/{id}")
	public void article(@PathVariable String id) throws CmsException, IOException{
		System system = systemService.getSystem();
		String staticdir = system.getStaticdir();
		if(staticdir.startsWith("/")) {
			staticdir = staticdir.substring(1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Archives archives = archivesService.selectByPrimaryKey(id);
		//如果为静态浏览，则重写向到静态文件
		if(2 == system.getBrowseType()) {
			Category temp = null;
			if(!"-1".equals(archives.getCategoryId())) {
				temp = categoryService.selectById(archives.getCategoryId());
			}else {//顶级分类走该模版
				temp = new Category();
				temp.setId("-1");
				temp.setCnname("顶级分类");
			}
			String catDir = URLUtils.getCategoryDir(temp);
			
			Date createTime = archives.getCreateTime();
			String dateDir = sdf.format(createTime);
			String url = URLUtils.parseFileName(null, 1);
			
			File staticFile = new File(fileConfiguration.getResourceDir() + staticdir + catDir + "/" + dateDir + "/" + archives.getId() + ".html");
			if(!staticFile.exists()) {
				throw new TemplateNotFoundException(
						ExceptionEnum.HTTP_NOT_FOUND.getCode(),
						ExceptionEnum.HTTP_NOT_FOUND.getMessage(),
						"当前浏览方式为静态浏览，您所浏览的静态文件不存在，请先静态化网站后再继续。");
			}
			response.sendRedirect("/" + system.getStaticdir() + catDir + "/" + dateDir + "/" + archives.getId() + ".html");
			return;
		}
		
		
		StringBuffer templatePath = new StringBuffer();
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		templatePath.append(theme.getThemePath());

		
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
				throw new TemplateNotFoundException(
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
						"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.parse(html);
			newHtml = parseEngine.parseCategory(newHtml, category.getCode());
			newHtml = parseEngine.parseArticle(newHtml, id);
			newHtml = parseEngine.parsePrevAndNext(newHtml, id);
			
			//更新点击数
			Archives temp = new Archives();
			temp.setId(id);
			temp.setClicks(archives.getClicks() + 1);
			archivesService.update(temp);
			
			//输出HTML
			outHtml(newHtml);
		} catch (IOException e) {
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 附件下载
	 * @param id
	 * @param request
	 * @param response
	 * @throws AdminGeneralException
	 */
	@RequestMapping("/download/{id}")
	public void download(@PathVariable String id, HttpServletRequest request,HttpServletResponse response) throws AdminGeneralException {
		try {
			System system = systemService.getSystem();
			Attachment attachment = attachmentService.queryAttachmentById(id);
		    //设置响应头和客户端保存文件名
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("multipart/form-data");
		    response.setHeader("Content-Disposition", "attachment;fileName=" + attachment.getFilename());
	        //打开本地文件流
		    String filePath = fileConfiguration.getResourceDir() + system.getUploaddir() + attachment.getFilepath();
	        InputStream inputStream = new FileInputStream(filePath);
	        //激活下载操作
	        OutputStream os = response.getOutputStream();
	        //循环写入输出流
	        byte[] b = new byte[1024];
	        int length;
	        while ((length = inputStream.read(b)) > 0) {
	            os.write(b, 0, length);
	        }

	        // 这里主要关闭。
	        os.close();
	        inputStream.close();
		}catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
	}
	
	/**
	 * 搜索
	 * @param model
	 * @param params
	 * @throws CmsException
	 */
	@RequestMapping(value = "/search")
	public void search(Model model, SearchEntity params) throws CmsException {
		System system = systemService.getSystem();
		StringBuffer templatePath = new StringBuffer();
		Theme theme = themeService.getCurrentTheme();
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		if(theme == null) {
			
		}
		templatePath.append(theme.getThemePath());
		templatePath.append("/search.html");

		if(params.getPageNum() == null)
			params.setPageNum(1);
		if(params.getPageSize() == null)
			params.setPageSize(10);
		
		try {
			Map<String, Object> entity = params.getEntity();
			if(entity == null || entity.size() <= 0) {
				throw new FormParameterException(
						ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
						ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
						"请仔细检查Form表单参数结构，正确参数格式应该包含entity['keywords']、pageNum、pageSize。");
			}
			String keywords = "";
			if(entity.containsKey("keywords")) {
				keywords = params.getEntity().get("keywords").toString();

				if(keywords.getBytes("GBK").length < 3) {
					throw new FormParameterException(
							ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
							ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
							"搜索关键字不能少于5个字符，请重新输入后进行搜索。");
				}
			}
			
			String path = templateDir + templatePath;
			File template = new File(path);
			if(!template.exists()) {
				throw new TemplateNotFoundException(
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
						"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
			}
			String newHtml = "";
			String html = FileUtils.readFileToString(template, "UTF-8");
			//如果为静态浏览，则生成页面
			if(2 == system.getBrowseType()) {
				newHtml = parseEngine.generate(html);
			}else {
				newHtml = parseEngine.parse(html);
			}
			newHtml = parseEngine.parsePageList(newHtml, params);
			
			//记录搜索关键词
			SearchRecord sr = new SearchRecord();
			sr.setId(UUIDUtils.getPrimaryKey());
			sr.setKeywords(keywords);
			sr.setCreateTime(new Date());
			searchRecordService.add(sr);
			
			//输出HTML
			outHtml(newHtml);
		} catch (IOException e) {
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 前端投稿
	 * @param model
	 * @param params
	 * @return
	 * @throws CmsException
	 */
	@RequestMapping(value = "/input",method = RequestMethod.POST)
	public String input(Model model, @RequestParam Map<String,Object> params) throws CmsException {
		// 验证码校验
		if(!params.containsKey("captcha") || StringUtil.isBlank(params.get("captcha"))) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"缺少验证码参数，请添加该参数后重试。");
		}
		if(!CaptchaUtil.ver(params.get("captcha").toString(), request)) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"验证码输入错误或已超时，请仔细检查后再试。");
		}
		System system = systemService.getSystem();
		if(!params.containsKey("typeid") || StringUtil.isBlank(params.get("typeid"))) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"缺少[typeid]参数，请添加该参数后重试。");
		}
		if(!params.containsKey("formkey") || StringUtil.isBlank(params.get("formkey"))) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"缺少[formkey]参数，请添加该参数后重试。");
		}
		
		String typeid = params.get("typeid").toString();
		String formkey = params.get("formkey").toString();
		
		Category categoryWithBLOBs = categoryService.queryCategoryByCode(typeid);
		if(categoryWithBLOBs == null) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"栏目不存在，请仔细检查[typeid]参数是否有误，核实后重试。");
		}
		
		if(categoryWithBLOBs.getIsInput() != 1) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"栏目不允许投稿，请仔细检查栏目的详情并设置是否允许投稿为是后重试。");
		}
		
		Form form = formService.queryFormByCode(formkey);
		if(form == null) {
			throw new FormParameterException(
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getCode(),
					ExceptionEnum.FORM_PARAMETER_EXCEPTION.getMessage(),
					"表单模型不存在，请仔细检查[formkey]参数是否有误，核实后重试。");
		}
		
		Archives archives = new Archives();
		archives.setId(UUIDUtils.getPrimaryKey());
		archives.setCreateTime(new Date());
		archives.setStatus(1);//未发布
		
		archives.setTitle(StringUtil.isBlank(params.get("title")) ? "" : params.get("title").toString());
		archives.setTag(StringUtil.isBlank(params.get("tag")) ? "" : params.get("tag").toString());
		archives.setCategoryId(categoryWithBLOBs.getId());
		archives.setCategoryIds(categoryWithBLOBs.getCatSeq());
		archives.setImagePath(StringUtil.isBlank(params.get("imagePath")) ? "" : params.get("imagePath").toString());
		archives.setWeight(StringUtil.isBlank(params.get("weight")) ? 0 : Integer.parseInt(params.get("weight").toString()));
		archives.setClicks(StringUtil.isBlank(params.get("clicks")) ? 0 : Integer.parseInt(params.get("clicks").toString()));
		archives.setDescription(StringUtil.isBlank(params.get("description")) ? "" : params.get("description").toString());
		archives.setComment(StringUtil.isBlank(params.get("comment")) ? 0 : Integer.parseInt(params.get("comment").toString()));
		archives.setSubscribe(StringUtil.isBlank(params.get("subscribe")) ? 0 : Integer.parseInt(params.get("subscribe").toString()));
		
		
		List<Field> fields = fieldService.queryFieldByFormId(form.getId());
		Map<String,Object> additional = new LinkedHashMap<String,Object>();
		additional.put("id", UUIDUtils.getPrimaryKey());
		additional.put("aid", archives.getId());
		for(int i = 0;i < fields.size();i++) {
			Field field = fields.get(i);
			additional.put(field.getFieldName(), params.get(field.getFieldName()));
			//用MAP接收参数，checkbox需要特殊处理
			if("checkbox".equals(field.getDataType())) {
				String[] arr = request.getParameterValues(field.getFieldName());
				if(arr != null && arr.length > 0) {
					StringBuffer checkboxVal = new StringBuffer();
					for (String string : arr) {
						checkboxVal.append(string + ",");
					}
					additional.put(field.getFieldName(), checkboxVal.substring(0, checkboxVal.length() - 1));
				}
			}
		}
		String tableName = "system_" + form.getTableName();
		
		try {
			archivesService.save(archives,tableName,additional);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		
		String typeUrl = URLUtils.parseURL(system, categoryWithBLOBs, "P");
		return "redirect:" + typeUrl;
	}
	
	// 产生验证码
	@RequestMapping("/getKaptcha")
	public void getKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5
		CaptchaUtil.out(captcha, request, response);
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
