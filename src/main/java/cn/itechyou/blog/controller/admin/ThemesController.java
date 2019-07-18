package cn.itechyou.blog.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.blog.common.BaseController;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Theme;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.service.ThemeService;
import cn.itechyou.blog.utils.FileConfiguration;
import cn.itechyou.blog.utils.SaxUtils;
import cn.itechyou.blog.utils.UUIDUtils;
import cn.itechyou.blog.utils.ZipUtils;

@Controller
@RequestMapping("/admin/theme")
public class ThemesController extends BaseController {
	
	private static final int buffer = 2048; 
	
	@Autowired
	private ThemeService themeService;
	
	@Autowired
	private FileConfiguration fileConfiguration;
	
	@RequestMapping("/list")
	public String list(Model model, SearchEntity params) {
		Map<String,Object> map = new HashMap<>();
		List<Theme> themes = themeService.queryListByPage(params);
		model.addAttribute("themes", themes);
		return "/admin/themes/list";
	}
	
	@RequestMapping("/add")
	public String add(String themePath) {
		InputStream in = null;
		Theme theme;
		try {
			String rootPath = fileConfiguration.getUploadDir();
			File f = new File(rootPath + File.separator + "system.properties");
			in = new FileInputStream(f);
			Properties properties = new Properties();
			properties.load(in);
			String filepath = properties.getProperty("uploaddir");
			
			String uploadpath = rootPath + File.separator + filepath + File.separator + themePath;
			File zipFile = new File(uploadpath);
			
			//解压zip
			String targetDir = ResourceUtils.getURL("classpath:").getPath();
			targetDir += File.separator + "templates/themes/";
			theme = ZipUtils.unZipFiles(zipFile, targetDir);
			
			SaxUtils.parseXml(theme.getThemePath() + File.separator + "main.xml");
			theme = SaxUtils.theme;
			theme.setId(UUIDUtils.getPrimaryKey());
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/admin/theme/list";
	}
	
	@RequestMapping("/updateStatus")
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
	public String delete(String id) {
		int rows = themeService.delete(id);
		return "redirect:/admin/theme/list";
	}
	
}
