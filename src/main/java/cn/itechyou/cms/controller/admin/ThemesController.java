package cn.itechyou.cms.controller.admin;

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

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.exception.FileFormatErrorException;
import cn.itechyou.cms.exception.FileNotFoundException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.utils.ZipUtils;

/**
 * 主题管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/theme")
public class ThemesController extends BaseController {
	private static final int buffer = 2048; 
	@Autowired
	private ThemeService themeService;
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemService systemService;
	
	@RequestMapping({"","/list"})
	@RequiresPermissions("t5atzaz6")
	public String list(Model model, SearchEntity params) {
		Map<String,Object> map = new HashMap<>();
		List<Theme> themes = themeService.queryListByPage(params);
		model.addAttribute("themes", themes);
		return "admin/themes/list";
	}
	
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
	
	@RequestMapping("/delete")
	@RequiresPermissions("4ng92074")
	public String delete(String id) {
		int rows = themeService.delete(id);
		return "redirect:/admin/theme/list";
	}
	
}
