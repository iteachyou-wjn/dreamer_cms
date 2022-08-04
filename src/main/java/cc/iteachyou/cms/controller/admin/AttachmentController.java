package cc.iteachyou.cms.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Attachment;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.AttachmentService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.utils.FileConfiguration;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 附件管理
 * @author Wangjn
 * @version 2.0.0
 */
@Controller
@RequestMapping("admin/attachment")
public class AttachmentController extends BaseController {
	
	@Autowired
	private FileConfiguration fileConfiguration;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private SystemService systemService;
	
	@Log(operType = OperatorType.PAGE, module = "附件管理", content = "附件分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("5z1mj7i6")
	public String toIndex(Model model ,SearchEntity params) {
		System system = systemService.getSystem();
		PageInfo<Attachment> page = attachmentService.queryListByPage(params);
		model.addAttribute("page", page);
		model.addAttribute("system", system);
		return "admin/attachment/list";
	}
	
	@Log(operType = OperatorType.INSERT, module = "附件管理", content = "添加附件")
	@RequestMapping("/add")
	@RequiresPermissions("ors4k771")
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
	
	@Log(operType = OperatorType.DELETE, module = "附件管理", content = "删除附件")
	@RequestMapping("/delete")
	@RequiresPermissions("7b3w257s")
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
	
	@Log(operType = OperatorType.OTHER, module = "附件管理", content = "下载附件")
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
