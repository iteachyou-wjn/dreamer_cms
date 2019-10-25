package cn.itechyou.cms.controller.admin;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.Json;
import cn.itechyou.cms.common.Result;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.ueditor.ActionEnter;
import cn.itechyou.cms.utils.DateUtils;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.UUIDUtils;

@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FileConfiguration fileConfiguration;

    @Autowired
    private SystemService systemService;

    /**
     * 文件上传
     * 
     * @param file
     */
    @RequestMapping("/uploadFile")
    public Result upload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return Json.badRequest(); //TODO
        }

        final Map<String, Object> result = new HashMap<>();
        String rootPath = fileConfiguration.getResourceDir();
        String currentDate = DateUtils.getCurrentDate("yyyyMMdd");
        System system = systemService.getSystem();
        String uploadDir = system.getUploaddir();

        File directory = new File(rootPath + File.separator + uploadDir + File.separator + currentDate);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String newFileName = UUIDUtils.getPrimaryKey() + file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));

        String absolutePath = directory.getAbsolutePath(); //获取绝对路径
        File uploadpath = new File(absolutePath + File.separator + newFileName);

        try {
            file.transferTo(uploadpath);
            result.put("filepath", currentDate + File.separator + newFileName);
            result.put("name", newFileName);
            result.put("url", system.getWebsite() + File.separator + uploadDir + File.separator + currentDate
                    + File.separator + newFileName);

            return Json.ok(result);
        }
        catch (Exception e) {
            return Json.failed();
        }
    }

    @ResponseBody
    @RequestMapping("uploadMarkDown")
    public Json editormdPic(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file)
            throws Exception //
    {
        if (file.isEmpty()) {
            return Json.badRequest(); //TODO
        }

        try {

            String rootPath = fileConfiguration.getResourceDir();
            String currentDate = DateUtils.getCurrentDate("yyyyMMdd");
            System system = systemService.getSystem();
            String uploadDir = system.getUploaddir();
            File directory = new File(rootPath + File.separator + uploadDir + File.separator + currentDate);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String newFileName = UUIDUtils.getPrimaryKey() + file.getOriginalFilename().substring(file
                    .getOriginalFilename().lastIndexOf("."));
            String absolutePath = directory.getAbsolutePath(); //获取绝对路径
            File uploadpath = new File(absolutePath + File.separator + newFileName);
            file.transferTo(uploadpath);
            String url = system.getWebsite() + File.separator + uploadDir + File.separator + currentDate
                    + File.separator + newFileName;
            return Json.ok("上传成功", url);
        }
        catch (Exception e) {
            return Json.failed("上传失败");
        }
    }

    @RequestMapping("ueditorConfig")
    public void ueditorConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        //        response.setHeader("Content-Type", "text/html");
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
