package cn.itechyou.cms.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Attachment;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.AttachmentService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * 附件管理
 * @author Wangjn
 * @version 2.0.0
 */
@Controller
@RequestMapping("/admin/attachment")
public class AttachmentController extends BaseController {
	
	@Autowired
	private FileConfiguration fileConfiguration;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/list")
	public String toIndex(Model model ,SearchEntity params) {
		PageInfo<Attachment> page = attachmentService.queryListByPage(params);
		model.addAttribute("attachments", page);
		return "admin/attachment/list";
	}
	
	@RequestMapping("/add")
	public String add(Attachment attachment) throws AdminGeneralException {
		try {
			attachment.setId(UUIDUtils.getPrimaryKey());
			attachment.setCode(UUIDUtils.getCharAndNumr(8));
			attachment.setCreateBy(TokenManager.getUserId());
			attachment.setCreateTime(new Date());
			attachment.setUpdateBy(TokenManager.getUserId());
			attachment.setUpdateTime(new Date());
			int rows = attachmentService.save(attachment);
		}catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/attachment/list";
	}
	
	@RequestMapping("/delete")
	public String delete(String id) throws AdminGeneralException {
		try {
			System system = systemService.getSystem();
			Attachment attachment = attachmentService.queryAttachmentById(id);
			File file = new File(fileConfiguration.getResourceDir() + system.getUploaddir() + attachment.getFilepath());
			if(file.exists()) {
				file.delete();
			}
			int rows = attachmentService.delete(id);
		}catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/attachment/list";
	}
	
	@RequestMapping("/download")
	public void download(String id,HttpServletRequest request,HttpServletResponse response) throws AdminGeneralException {
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
}
