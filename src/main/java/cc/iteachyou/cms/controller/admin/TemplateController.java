package cc.iteachyou.cms.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Theme;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.TemplatePermissionDeniedException;
import cc.iteachyou.cms.service.ThemeService;
import cc.iteachyou.cms.utils.FileConfiguration;
import cc.iteachyou.cms.vo.TemplateVo;

/**
 * 模板管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("admin/templates")
public class TemplateController {
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private ThemeService themeService;
	
	@Log(operType = OperatorType.SELECT, module = "模板管理", content = "模板列表")
	@RequestMapping({"","toIndex"})
	@RequiresPermissions("psqg04kn")
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
	
	@Log(operType = OperatorType.SELECT, module = "模板管理", content = "模板详情")
	@GetMapping("toView")
	@RequiresPermissions("5dg093r8")
	public ModelAndView toView(String path,String fileName) throws IOException,CmsException {
		ModelAndView mv = new ModelAndView();
		String themeDirPath = path + File.separator + fileName + File.separator;
		File themeDirPathFile = new File(themeDirPath);
		
		/**
		 * 查询当前模版目录，判断是否为模版目录，如不是，则报错
		 */
		Theme currentTheme = themeService.getCurrentTheme();
		String resourceDir = fileConfiguration.getResourceDir();
		String themePath = resourceDir + File.separator + "templates" + File.separator + currentTheme.getThemePath() + File.separator;
		themePath = themePath.replaceAll("\\*", "/");
		File themeDir = new File(themePath);
		if(!themeDirPathFile.getCanonicalPath().startsWith(themeDir.getCanonicalPath())) {
			throw new TemplatePermissionDeniedException(StateCodeEnum.HTTP_FORBIDDEN.getCode(), StateCodeEnum.HTTP_FORBIDDEN.getDescription(), "您没有操作权限！");
		}
				
		File[] listFiles = themeDirPathFile.listFiles();
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
	
	@Log(operType = OperatorType.OTHER, module = "模板管理", content = "修改模板页面")
	@GetMapping("toEdit")
	@RequiresPermissions("3oc5ri29")
	public ModelAndView toEdit(String path,String file) throws IOException, CmsException {
		ModelAndView mv = new ModelAndView();
		String fileName = path + File.separator + file;
		File templateFile = new File(fileName);
		/**
		 * 查询当前模版目录，判断是否为模版目录，如不是，则报错
		 */
		Theme currentTheme = themeService.getCurrentTheme();
		String resourceDir = fileConfiguration.getResourceDir();
		String themePath = resourceDir + File.separator + "templates" + File.separator + currentTheme.getThemePath() + File.separator;
		themePath = themePath.replaceAll("\\*", "/");
		File themeDir = new File(themePath);
		if(!templateFile.getCanonicalPath().startsWith(themeDir.getCanonicalPath())) {
			throw new TemplatePermissionDeniedException(StateCodeEnum.HTTP_FORBIDDEN.getCode(), StateCodeEnum.HTTP_FORBIDDEN.getDescription(), "您没有操作权限！");
		}
		String content = FileUtils.readFileToString(templateFile, "UTF-8");
		mv.addObject("content", content);
		mv.addObject("path", path);
		mv.addObject("file", file);
		mv.setViewName("admin/template/edit");
		return mv;
	}
	
	@Log(operType = OperatorType.UPDATE, module = "模板管理", content = "修改模板")
	@PostMapping("save")
	@RequiresPermissions("5n6ta53y")
	public String save(TemplateVo template) throws IOException {
		String filePath = template.getPath() + File.separator + template.getFile();
		filePath = filePath.replaceAll("\\*", "/");
		File file = new File(filePath);
		FileUtils.writeStringToFile(file, template.getContent(), "UTF-8");
		return "redirect:/admin/templates/toIndex";
	}
	
}
