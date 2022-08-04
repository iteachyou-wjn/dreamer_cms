package cc.iteachyou.cms.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.entity.Theme;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.FileFormatErrorException;
import cc.iteachyou.cms.exception.FileNotFoundException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.service.ThemeService;
import cc.iteachyou.cms.utils.FileConfiguration;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;
import cc.iteachyou.cms.utils.ZipUtils;

/**
 * 主题管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/theme")
public class ThemesController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ThemeService themeService;
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemService systemService;
	
	@Log(operType = OperatorType.PAGE, module = "主题管理", content = "主题分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("t5atzaz6")
	public String list(Model model, SearchEntity params) {
		System system = systemService.getSystem();
		List<Theme> themes = themeService.queryListByPage(params);
		model.addAttribute("themes", themes);
		model.addAttribute("system", system);
		return "admin/themes/list";
	}
	
	@Log(operType = OperatorType.INSERT, module = "主题管理", content = "添加主题")
	@RequestMapping("/add")
	@RequiresPermissions("j8rj0lp8")
	public String add(String themePath) throws IOException, CmsException {
		Theme theme;
		String rootPath = fileConfiguration.getResourceDir();
		System system = systemService.getSystem();
		String uploadDir = system.getUploaddir();
		
		String uploadpath = rootPath + File.separator + uploadDir + File.separator + themePath;
		File zipFile = new File(uploadpath);
		
		//解压zip
		String targetDir = rootPath + "templates/";
		theme = ZipUtils.unZipFiles(zipFile, targetDir);
		String themeJsonFilePath = theme.getThemePath() + File.separator + "theme.json";
		File themeJsonFile = new File(themeJsonFilePath);
		if(!themeJsonFile.exists()) {
			throw new FileNotFoundException(
					ExceptionEnum.FILE_NOTFOUND_EXCEPTION.getCode(), 
					ExceptionEnum.FILE_NOTFOUND_EXCEPTION.getMessage(),
					"主题包中不包含theme.json描述文件，请仔细检查主题包再上传。");
		}
		String jsonString = FileUtils.readFileToString(themeJsonFile, "UTF-8");
		JSONObject jsonObject = JSON.parseObject(jsonString);
		if(jsonObject == null) {
			throw new FileFormatErrorException(
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getCode(), 
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getMessage(),
					"主题描述文件theme.json格式错误，请仔细检查描述文件内容再上传。");
		}
		if(!jsonObject.containsKey("themeName") 
				|| !jsonObject.containsKey("themeImage") 
				|| !jsonObject.containsKey("themeAuthor")
				|| !jsonObject.containsKey("themePath")) {
			throw new FileFormatErrorException(
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getCode(), 
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getMessage(),
					"主题描述文件theme.json格式错误，请仔细检查描述文件内容再上传。");
		}
		String themeName = jsonObject.getString("themeName");
		String themeImage = jsonObject.getString("themeImage");
		String themeAuthor = jsonObject.getString("themeAuthor");
		String themePath1 = jsonObject.getString("themePath");
		if(StringUtil.isBlank(themeName) 
				|| StringUtil.isBlank(themeImage) 
				|| StringUtil.isBlank(themeAuthor) 
				|| StringUtil.isBlank(themePath1)) {
			throw new FileFormatErrorException(
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getCode(), 
					ExceptionEnum.FILE_FORMAT_ERROR_EXCEPTION.getMessage(),
					"主题描述文件theme.json格式错误，请仔细检查描述文件内容是否有为空的项。");
		}
		
		theme.setId(UUIDUtils.getPrimaryKey());
		theme.setThemeName(themeName);
		theme.setThemeImg(themeImage);
		theme.setThemeAuthor(themeAuthor);
		theme.setThemePath(themePath1);
		theme.setCreateBy(TokenManager.getToken().getId());
		theme.setCreateTime(new Date());
		theme.setStatus(0);
		
		if("default".equals(theme.getThemePath())) {
			throw new RuntimeException("默认模版不可覆盖！");
		}
		List<Theme> list = themeService.queryByPathName(theme.getThemePath());
		if(list != null && list.size() > 0) {
			Theme oldTheme = list.get(0);
			theme.setId(oldTheme.getId());
			theme.setUpdateBy(TokenManager.getToken().getId());
			theme.setUpdateTime(new Date());
			theme.setStatus(null);
			themeService.update(theme);
		}else {
			int row = themeService.save(theme);
		}
		return "redirect:/admin/theme/list";
	}
	
	@Log(operType = OperatorType.UPDATE, module = "主题管理", content = "修改主题状态")
	@RequestMapping("/updateStatus")
	@RequiresPermissions("g1u4y47a")
	public String updateStatus(String id, int status) {
		Theme theme = new Theme();
		theme.setId(id);
		theme.setStatus(status);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		if(status == 1) {//如果为启用，则需要将其它主题设置成禁用
			params.put("status", 0);
			themeService.batchUpdateStatus(params);
		}
		
		int rows = themeService.update(theme);
		return "redirect:/admin/theme/list";
	}
	
	@Log(operType = OperatorType.UPDATE, module = "主题管理", content = "修改主题")
	@RequestMapping("/update")
	@RequiresPermissions("g1u4y47a")
	public String update(Theme theme) {
		Theme temp = new Theme();
		temp.setId(theme.getId());
		temp.setThemeImg(theme.getThemeImg());
		int rows = themeService.update(temp);
		return "redirect:/admin/theme/list";
	}
	
	@Log(operType = OperatorType.DELETE, module = "主题管理", content = "删除主题")
	@RequestMapping("/delete")
	@RequiresPermissions("4ng92074")
	public String delete(String id) {
		int rows = themeService.delete(id);
		return "redirect:/admin/theme/list";
	}
	
}
