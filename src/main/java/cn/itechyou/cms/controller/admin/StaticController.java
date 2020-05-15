package cn.itechyou.cms.controller.admin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.EncodeException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.Message;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.exception.TemplateNotFoundException;
import cn.itechyou.cms.exception.TemplateReadException;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.taglib.ParseEngine;
import cn.itechyou.cms.taglib.utils.URLUtils;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.vo.CategoryVo;
import cn.itechyou.cms.websocket.WebSocketServer;

/**
 * 静态化管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("/admin/static")
public class StaticController {
	
	@Autowired
	private ThemeService themeService;
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private ParseEngine parseEngine;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArchivesService archivesService;
	
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping("toIndex")
	public ModelAndView toIndex() {
		System system = systemService.getSystem();
		ModelAndView mv = new ModelAndView();
		Theme currentTheme = themeService.getCurrentTheme();
		mv.addObject("currentTheme", currentTheme);
		List<CategoryWithBLOBs> list = categoryService.selectByParentId("-1");
		List<CategoryWithBLOBs> newTree = buildTree(list, 1);
		mv.addObject("categorys", newTree);
		mv.addObject("system", system);
		mv.setViewName("admin/static/index");
		return mv;
	}
	
	/**
	 * 生成首页
	 * @param userId
	 * @return
	 * @throws CmsException
	 * @throws IOException
	 * @throws EncodeException
	 */
	@RequestMapping("generateIndex/{clientId}")
	@ResponseBody
	public ResponseResult generateIndex(@PathVariable String clientId) throws CmsException, IOException, EncodeException {
		Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"准备生成首页HTML...",3);
		WebSocketServer.sendInfo(message,clientId);
		System system = systemService.getSystem();
		Theme theme = themeService.getCurrentTheme();
		String templatePath = theme.getThemePath() + "/index.html";
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		String path = templateDir + templatePath;
		File template = new File(path);
		message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"读取首页模版文件...",15);
		WebSocketServer.sendInfo(message,clientId);
		if(!template.exists() || template.isDirectory()) {
			message = new Message(StateCodeEnum.HTTP_ERROR.getCode(),"读取首页模版文件失败...",15);
			WebSocketServer.sendInfo(message,clientId);
			throw new TemplateNotFoundException(
					ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
					"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
		String newHtml = "";
		try {
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"开始解析首页模版标签...",35);
			WebSocketServer.sendInfo(message,clientId);
			String html = FileUtils.readFileToString(template, "UTF-8");
			newHtml = parseEngine.generate(html);
			File file = new File(fileConfiguration.getResourceDir() + system.getStaticdir() + "/index.html");
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"解析完成，准备生成静态HTML文件...",90);
			WebSocketServer.sendInfo(message,clientId);
			FileUtils.write(file, newHtml);
		} catch (IOException e) {
			message = new Message(StateCodeEnum.HTTP_ERROR.getCode(),"解析首页模版文件失败...",90);
			WebSocketServer.sendInfo(message,clientId);
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
		message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"一键生成首页静态HTML文件成功...",100);
		WebSocketServer.sendInfo(message,clientId);
        return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
	}
	
	/**
	 * 生成栏目静态化
	 * @param clientId
	 * @param categoryVo
	 * @return
	 * @throws CmsException
	 * @throws IOException
	 * @throws EncodeException
	 */
	@RequestMapping("generateCategory/{clientId}")
	@ResponseBody
	public ResponseResult generateCategory(@PathVariable String clientId, @RequestBody CategoryVo categoryVo) throws CmsException, IOException, EncodeException {
		Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"准备生成栏目HTML...",3);
		WebSocketServer.sendInfo(message,clientId);
		String id = categoryVo.getId();
		String updateChild = categoryVo.getUpdateChild();//是否生成子栏目
		if(StringUtil.isBlank(id)) {//生成全部
			List<CategoryWithBLOBs> all = categoryService.queryAll();
			for (Iterator iterator = all.iterator(); iterator.hasNext();) {
				CategoryWithBLOBs categoryWithBLOBs = (CategoryWithBLOBs) iterator.next();
				message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"准备生成" + categoryWithBLOBs.getCnname() + "栏目HTML...",10);
				WebSocketServer.sendInfo(message,clientId);
				buildCategoryHTML(categoryWithBLOBs, "ALL", clientId);//
			}
		}else {//指定栏目生成
			CategoryWithBLOBs categoryWithBLOBs = categoryService.selectById(id);
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"准备生成" + categoryWithBLOBs.getCnname() + "栏目HTML...",15);
			WebSocketServer.sendInfo(message,clientId);
			buildCategoryHTML(categoryWithBLOBs, updateChild, clientId);
		}
		message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"全部栏目生成静态HTML文件成功...",100);
		WebSocketServer.sendInfo(message,clientId);
		return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
	}
	
	/**
	 * 构建栏目html并生成文件
	 * @param categoryWithBLOBs
	 * @param clientId
	 * @throws TemplateNotFoundException
	 * @throws TemplateReadException
	 * @throws EncodeException
	 * @throws IOException
	 */
	private void buildCategoryHTML(CategoryWithBLOBs categoryWithBLOBs, String updateChild, String clientId)
			throws TemplateNotFoundException, TemplateReadException, EncodeException, IOException {
		System system = systemService.getSystem();
		Theme theme = themeService.getCurrentTheme();
		
		if("ALL".equals(updateChild) || "1".equals(updateChild)) {
			List<CategoryWithBLOBs> childrenList = categoryService.selectByParentId(categoryWithBLOBs.getId());
			if(childrenList != null && childrenList.size() > 0) {
				for(int i = 0;i < childrenList.size();i++) {
					buildCategoryHTML(childrenList.get(i), updateChild, clientId);
				}
			}
		}
		
		String templateDir = fileConfiguration.getResourceDir() + "templates/";
		StringBuffer templatePath = new StringBuffer();
		templatePath.append(theme.getThemePath());
		if(categoryWithBLOBs.getCatModel() == 1) {
			templatePath.append(categoryWithBLOBs.getCoverTemp());
		}else if(categoryWithBLOBs.getCatModel() == 2) {
			templatePath.append(categoryWithBLOBs.getListTemp());
		}else if(categoryWithBLOBs.getCatModel() == 3) {
			return;
		}
		String newHtml = "";
		try {
			Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"读取栏目模版文件...",15);
			WebSocketServer.sendInfo(message,clientId);
			String path = templateDir + templatePath;
			File template = new File(path);
			if(!template.exists() || template.isDirectory()) {
				message = new Message(StateCodeEnum.HTTP_ERROR.getCode(),"读取[" + categoryWithBLOBs.getCnname() + "]模版文件失败...",15);
				WebSocketServer.sendInfo(message,clientId);
				throw new TemplateNotFoundException(
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getCode(),
						ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION.getMessage(),
						"请仔细检查" + template.getAbsolutePath() + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
			}
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"开始解析" + categoryWithBLOBs.getCnname() + "模版标签...",35);
			WebSocketServer.sendInfo(message,clientId);
			String html = FileUtils.readFileToString(template, "UTF-8");
			
			newHtml = parseEngine.generate(html);
			newHtml = parseEngine.generateCategory(newHtml, categoryWithBLOBs.getCode());
			
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"解析完成，准备生成静态HTML文件...",70);
			/**
			 * 如果为列表栏目，则循环生成分页页面
			 */
			if(categoryWithBLOBs.getCatModel() == 2) {//列表
				SearchEntity params = new SearchEntity();
				params.setPageNum(1);
				params.setPageSize(StringUtil.isBlank(categoryWithBLOBs.getPageSize()) ? 10 : categoryWithBLOBs.getPageSize());
				Map<String,Object> entity = new HashMap<String,Object>();
				entity.put("cid", categoryWithBLOBs.getCode());
				params.setEntity(entity);
				PageInfo<Map<String, Object>> pageInfo = archivesService.queryListByPage(params);
				int total = pageInfo.getPages();
				int pageNum = pageInfo.getPageNum();
				int pageSize = pageInfo.getPageSize();
				while (pageNum <= total) {
					message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"开始生成[" + categoryWithBLOBs.getCnname() + "]栏目，第["+pageNum+"]页静态HTML文件...",75);
					WebSocketServer.sendInfo(message,clientId);
					newHtml = parseEngine.generatePageList(newHtml, categoryWithBLOBs.getCode(), pageNum, pageSize);
					String catDir = URLUtils.getCategoryDir(categoryWithBLOBs);
					String fileName = URLUtils.parseFileName(categoryWithBLOBs, pageNum);
					File file = new File(fileConfiguration.getResourceDir() + system.getStaticdir() + catDir + fileName);
					FileUtils.write(file, newHtml);
					buildArticleHTML(categoryWithBLOBs,clientId);
					pageNum++;
				}
			}else {//封面
				WebSocketServer.sendInfo(message,clientId);
				String catDir = URLUtils.getCategoryDir(categoryWithBLOBs);
				File file = new File(fileConfiguration.getResourceDir() + system.getStaticdir() + catDir + "/index.html");
				FileUtils.write(file, newHtml);
			}
		} catch (IOException e) {
			Message message = new Message(StateCodeEnum.HTTP_ERROR.getCode(),"解析[" + categoryWithBLOBs.getCnname() + "]模版文件错误...",70);
			WebSocketServer.sendInfo(message,clientId);
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
		Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"生成" + categoryWithBLOBs.getCnname() + "静态HTML文件成功...",80);
		WebSocketServer.sendInfo(message,clientId);
	}
	
	/**
	 * 生成栏目下的文档
	 * @param categoryWithBLOBs
	 * @param clientId
	 * @throws TemplateNotFoundException
	 * @throws TemplateReadException
	 * @throws EncodeException
	 * @throws IOException
	 */
	private void buildArticleHTML(CategoryWithBLOBs categoryWithBLOBs, String clientId)
			throws TemplateNotFoundException, TemplateReadException, EncodeException, IOException {
		System system = systemService.getSystem();
		Theme theme = themeService.getCurrentTheme();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"开始生成[" + categoryWithBLOBs.getCnname() + "]栏目文档静态HTML文件...",78);
			WebSocketServer.sendInfo(message,clientId);
			String templateDir = fileConfiguration.getResourceDir() + "templates/";
			StringBuffer templatePath = new StringBuffer();
			templatePath.append(theme.getThemePath());
			
			if(categoryWithBLOBs.getCatModel() == 1) {
				return;
			}else if(categoryWithBLOBs.getCatModel() == 2) {
				templatePath.append(categoryWithBLOBs.getArticleTemp());
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
			String catDir = URLUtils.getCategoryDir(categoryWithBLOBs);
			
			List<Archives> allArt = this.archivesService.queryAll(categoryWithBLOBs.getId());
			
			for (int i = 0; i < allArt.size(); i++) {//循环生成文档
				message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"开始生成[" + categoryWithBLOBs.getCnname() + "]栏目第["+(i+1)+"]个文档静态HTML文件...",79);
				WebSocketServer.sendInfo(message,clientId);
				Archives archives = allArt.get(i);
				Date createTime = archives.getCreateTime();
				String dateDir = sdf.format(createTime);
				newHtml = parseEngine.parse(html);
				newHtml = parseEngine.parseCategory(newHtml, categoryWithBLOBs.getCode());
				newHtml = parseEngine.parseArticle(newHtml, archives.getId());
				File file = new File(fileConfiguration.getResourceDir() + system.getStaticdir() + catDir + "/" + dateDir + "/" + archives.getId() + ".html");
				FileUtils.write(file, newHtml);
				message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"生成[" + categoryWithBLOBs.getCnname() + "]栏目第["+(i+1)+"]个文档静态HTML文件成功...",79);
				WebSocketServer.sendInfo(message,clientId);
			}
			message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"生成[" + categoryWithBLOBs.getCnname() + "]栏目文档静态HTML文件成功...",78);
			WebSocketServer.sendInfo(message,clientId);
		}catch (Exception e) {
			Message message = new Message(StateCodeEnum.HTTP_ERROR.getCode(),"解析[" + categoryWithBLOBs.getCnname() + "]模版文件错误...",70);
			WebSocketServer.sendInfo(message,clientId);
			throw new TemplateReadException(
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getCode(),
					ExceptionEnum.TEMPLATE_READ_EXCEPTION.getMessage(),
					"请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
		}
	}
	
	/**
	 * 构建栏目树
	 * @param list
	 * @param i
	 * @return
	 */
	private List<CategoryWithBLOBs> buildTree(List<CategoryWithBLOBs> list, int i) {
		for (int j = list.size() - 1; j >= 0; j--) {
			CategoryWithBLOBs categoryWithBLOBs = list.get(j);
			StringBuilder sb = new StringBuilder();
			for (int k = 1; k < i; k++) {
				sb.append("--");
			}
			sb.append(categoryWithBLOBs.getCnname());
			categoryWithBLOBs.setCnname(sb.toString());
			List<CategoryWithBLOBs> children = categoryService.selectByParentId(categoryWithBLOBs.getId());
			List<CategoryWithBLOBs> buildTree = buildTree(children, i + 1);
			list.addAll(j + 1, buildTree);
		}
		return list;
	}
	
}
