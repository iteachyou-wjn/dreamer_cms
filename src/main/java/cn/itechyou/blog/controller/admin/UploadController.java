package cn.itechyou.blog.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.itechyou.blog.common.BaseController;
import cn.itechyou.blog.common.DateUtil;
import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.entity.System;
import cn.itechyou.blog.service.SystemService;
import cn.itechyou.blog.ueditor.ActionEnter;
import cn.itechyou.blog.utils.FileConfiguration;
import cn.itechyou.blog.utils.UUIDUtils;

@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController{
	
	@Autowired
	private FileConfiguration fileConfiguration;
	@Autowired
	private SystemService systemService;
	
	/**
	 *   文件上传
	 * @param file
	 */
	@RequestMapping("/uploadFile")
	public void upload(@RequestParam("file") MultipartFile file) {
		ResponseResult respResult = null;
		JSONObject result = new JSONObject();
		try {
			if(file.isEmpty()) {
				return;
			}
			String rootPath = fileConfiguration.getResourceDir();
			String currentDate = DateUtil.getCurrentDate("yyyyMMdd");
			System system = systemService.getSystem();
			String uploadDir = system.getUploaddir();
			File directory  = new File(rootPath + File.separator + uploadDir + File.separator + currentDate); 
			if(!directory.exists()){
				directory.mkdirs();
			}
			String newFileName = UUIDUtils.getPrimaryKey() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String absolutePath = directory.getAbsolutePath(); //获取绝对路径
			File uploadpath = new File(absolutePath + File.separator + newFileName);
			file.transferTo(uploadpath);
			result.put("filepath", currentDate+File.separator + newFileName);
			result.put("name", newFileName);
			result.put("url", system.getWebsite() + File.separator + uploadDir + File.separator + currentDate + File.separator + newFileName);
			respResult = ResponseResult.Factory.newInstance(Boolean.TRUE,
					StateCodeEnum.HTTP_SUCCESS.getCode(), result,
					StateCodeEnum.HTTP_SUCCESS.getDescription());
		} catch (Exception e) {
			respResult = ResponseResult.Factory.newInstance(Boolean.TRUE,
					StateCodeEnum.HTTP_ERROR.getCode(), null,
					StateCodeEnum.HTTP_ERROR.getDescription());
			e.printStackTrace();
		}
		this.outJson(respResult);
	}
	
	@RequestMapping("uploadMarkDown")
	@ResponseBody
	public void editormdPic(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject result = new JSONObject();
		try {
			if(file.isEmpty()) {
				return;
			}
			String rootPath = fileConfiguration.getResourceDir();
			String currentDate = DateUtil.getCurrentDate("yyyyMMdd");
			System system = systemService.getSystem();
			String uploadDir = system.getUploaddir();
			File directory  = new File(rootPath + File.separator + uploadDir + File.separator + currentDate); 
			if(!directory.exists()){
				directory.mkdirs();
			}
			String newFileName = UUIDUtils.getPrimaryKey() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String absolutePath = directory.getAbsolutePath(); //获取绝对路径
			File uploadpath = new File(absolutePath + File.separator + newFileName);
			file.transferTo(uploadpath);
			result.put("message", "上传成功");
			result.put("success", 1);
			result.put("url", system.getWebsite() + File.separator + uploadDir + File.separator + currentDate + File.separator + newFileName);
		} catch (Exception e) {
			result.put("message", "上传失败");
			result.put("success", 0);
			result.put("url", "");
			e.printStackTrace();
		}
		this.outJson(result);
	}
	
	@RequestMapping("ueditorConfig")
	public void ueditorConfig(HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Content-Type", "text/html");
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//String rootPath = ResourceUtils.getURL("classpath:").getPath();
		String rootPath = fileConfiguration.getResourceDir();
		String exec = new ActionEnter(request, rootPath).exec();
		PrintWriter writer = response.getWriter();
		writer.write(exec);
		writer.flush();
		writer.close();
	}
}
