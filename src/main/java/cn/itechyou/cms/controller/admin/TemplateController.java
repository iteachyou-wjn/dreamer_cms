package cn.itechyou.cms.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.vo.TemplateVo;

@Controller
@RequestMapping("admin/templates")
public class TemplateController {
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private ThemeService themeService;
	
	@RequestMapping("toIndex")
	public ModelAndView toIndex() {
		ModelAndView mv = new ModelAndView();
		Theme currentTheme = themeService.getCurrentTheme();
		String resourceDir = fileConfiguration.getResourceDir();
		String themePath = resourceDir + File.separator + "templates" + File.separator + currentTheme.getThemePath() + File.separator;
		themePath = themePath.replaceAll("\\*", "/");
		
		File themeDir = new File(themePath);
		File[] listFiles = themeDir.listFiles();
		
		List<Map<String,String>> list = new LinkedList<Map<String,String>>();
		for (File file : listFiles) {
			Map<String,String> map = new HashMap<String,String>();
			String fileName = file.getName();
			map.put("path", file.getParent());
			map.put("fileName", fileName);
			String extName = fileName.substring(fileName.lastIndexOf(".") == -1 ? 0 : fileName.lastIndexOf("."));
			map.put("extName", extName);
			if(file.isDirectory()) {
				map.put("type", "directory");
				list.add(0, map);
			}else if(file.isFile()) {
				map.put("type", "file");
				list.add(map);
			}
		}
		
		mv.addObject("list", list);
		mv.setViewName("admin/template/list");
		return mv;
	}
	
	@GetMapping("toView")
	public ModelAndView toView(String path,String fileName) throws IOException {
		ModelAndView mv = new ModelAndView();
		String themePath = path + File.separator + fileName + File.separator;
		File themeDir = new File(themePath);
		File[] listFiles = themeDir.listFiles();
		List<Map<String,String>> list = new LinkedList<Map<String,String>>();
		for (File file : listFiles) {
			Map<String,String> map = new HashMap<String,String>();
			String fn = file.getName();
			map.put("path", file.getParent());
			map.put("fileName", fn);
			String extName = fn.substring(fn.lastIndexOf(".") == -1 ? 0 : fn.lastIndexOf("."));
			map.put("extName", extName);
			if(file.isDirectory()) {
				map.put("type", "directory");
				list.add(0, map);
			}else if(file.isFile()) {
				map.put("type", "file");
				list.add(map);
			}
		}
		mv.addObject("list", list);
		mv.setViewName("admin/template/list");
		return mv;
	}
	
	@GetMapping("toEdit")
	public ModelAndView toEdit(String path,String file) throws IOException {
		ModelAndView mv = new ModelAndView();
		String fileName = path + File.separator + file;
		String content = FileUtils.readFileToString(new File(fileName), "UTF-8");
		mv.addObject("content", content);
		mv.addObject("path", path);
		mv.addObject("file", file);
		mv.setViewName("admin/template/edit");
		return mv;
	}
	
	@PostMapping("save")
	public String save(TemplateVo template) throws IOException {
		String filePath = template.getPath() + File.separator + template.getFile();
		filePath = filePath.replaceAll("\\*", "/");
		File file = new File(filePath);
		FileUtils.writeStringToFile(file, template.getContent(), "UTF-8");
		return "redirect:/admin/templates/toIndex";
	}
	
}
