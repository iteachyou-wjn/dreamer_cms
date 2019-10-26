/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.controller;

import static cn.itechyou.cms.common.Constant.DEFAULT_CHARSET;
import static cn.itechyou.cms.common.ExceptionEnum.TEMPLATE_NOTFOUND_EXCEPTION;
import static cn.itechyou.cms.common.ExceptionEnum.TEMPLATE_READ_EXCEPTION;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.SearchRecord;
import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.exception.TemplateNotFoundException;
import cn.itechyou.cms.exception.TemplateReadException;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.FieldService;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.PagesService;
import cn.itechyou.cms.service.SearchRecordService;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.taglib.ParseEngine;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.vo.ArchivesVo;

@Controller
@RequestMapping("/")
public class FrontController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private PagesService pagesService;
    @Autowired
    private FormService formService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private SearchRecordService searchRecordService;
    @Autowired
    private FileConfiguration fileConfiguration;

    @Autowired
    private ParseEngine parseEngine;

    /**
     * 首页方法
     * 
     * @throws CmsException
     */
    @RequestMapping("/index")
    public void index() throws CmsException {

        final String path = new StringBuilder()
                .append(fileConfiguration.getResourceDir())
                .append("templates/")
                .append(themeService.getCurrentTheme().getThemePath())
                .append("/index.html").toString();

        File template = new File(path);
        if (!template.exists()) {
            throw new TemplateNotFoundException(TEMPLATE_NOTFOUND_EXCEPTION,
                                                "请仔细检查" + path + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }

        try {
            outHtml(parseEngine.parse(FileUtils.readFileToString(template, DEFAULT_CHARSET)));
        }
        catch (IOException e) {
            throw new TemplateReadException(TEMPLATE_READ_EXCEPTION,
                                            "请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }
    }

    /**
     * 封面方法
     * 
     * @param typeid
     *            栏目编码
     * @param visitUrl
     *            访问URL
     * @throws CmsException
     */
    @RequestMapping("cover-{typeid}/{visitUrl}")
    public void cover(@PathVariable String typeid, @PathVariable String visitUrl) throws CmsException {
        Theme theme = themeService.getCurrentTheme();
        //        String templateDir = fileConfiguration.getResourceDir() + "templates/";

        //        if (theme == null) {
        //        }
        //        if (!visitUrl.startsWith("/")) {
        //            visitUrl = "/" + visitUrl;
        //        }

        CategoryWithBLOBs category = categoryService.queryCategoryByCode(typeid);

        final StringBuilder builder = new StringBuilder()
                .append(fileConfiguration.getResourceDir())
                .append("templates/")
                .append(theme.getThemePath());

        if (category.getCatModel() == 1) {
            builder.append(category.getCoverTemp());
        }
        else if (category.getCatModel() == 2) {
            builder.append(category.getListTemp());
        }
        else if (category.getCatModel() == 3) {
            builder.append(category.getLinkUrl());
        }

        final String path = builder.toString();
        File template = new File(path);
        if (!template.exists()) {
            throw new TemplateNotFoundException(TEMPLATE_NOTFOUND_EXCEPTION,
                                                "请仔细检查" + path +
                                                        "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }

        try {
            final String html = FileUtils.readFileToString(template, DEFAULT_CHARSET);
            outHtml(parseEngine.parseCategory(parseEngine.parse(html), typeid));
        }
        catch (IOException e) {
            throw new TemplateReadException(TEMPLATE_READ_EXCEPTION,
                                            "请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }
    }

    /**
     * 列表方法
     * 
     * @param typeid
     *            栏目编码
     * @param visitUrl
     *            访问URL
     * @param pageNum
     *            当前页
     * @param pageSize
     *            分页大小
     * @throws CmsException
     */
    @RequestMapping("list-{typeid}/{visitUrl}/{pageNum}/{pageSize}")
    public void list(@PathVariable String typeid,
                     @PathVariable String visitUrl,
                     @PathVariable Integer pageNum,
                     @PathVariable Integer pageSize) throws CmsException //
    {
        Theme theme = themeService.getCurrentTheme();
        String templateDir = fileConfiguration.getResourceDir() + "templates/";
        if (theme == null) {

        }
        if (!visitUrl.startsWith("/")) {
            visitUrl = "/" + visitUrl;
        }
        CategoryWithBLOBs category = categoryService.queryCategoryByCode(typeid);
        StringBuilder templatePath = new StringBuilder();
        templatePath.append(theme.getThemePath());

        if (category.getCatModel() == 1) {
            templatePath.append(category.getCoverTemp());
        }
        else if (category.getCatModel() == 2) {
            templatePath.append(category.getListTemp());
        }
        else if (category.getCatModel() == 3) {
            templatePath.append(category.getLinkUrl());
        }
        try {
            String path = templateDir + templatePath;
            File template = new File(path);
            if (!template.exists()) {
                throw new TemplateNotFoundException(TEMPLATE_NOTFOUND_EXCEPTION,
                                                    "请仔细检查" + template.getAbsolutePath()
                                                            + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
            }
            String newHtml = "";
            String html = FileUtils.readFileToString(template, DEFAULT_CHARSET);
            newHtml = parseEngine.parse(html);
            newHtml = parseEngine.parseCategory(newHtml, typeid);
            newHtml = parseEngine.parsePageList(newHtml, typeid, pageNum, pageSize);
            outHtml(newHtml);
        }
        catch (IOException e) {
            throw new TemplateReadException(TEMPLATE_READ_EXCEPTION,
                                            "请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }
    }

    /**
     * 文章详情方法
     * 
     * @param id
     *            文章ID
     * @throws CmsException
     */
    @RequestMapping("/article/{id}")
    public void article(@PathVariable String id) throws CmsException {

        StringBuilder templatePath = new StringBuilder();
        Theme theme = themeService.getCurrentTheme();
        String templateDir = fileConfiguration.getResourceDir() + "templates/";
        if (theme == null) {

        }
        templatePath.append(theme.getThemePath());

        Archives archives = archivesService.selectByPrimaryKey(id);
        String formId = formService.queryDefaultForm().getId();
        Category category = null;
        if (!"-1".equals(archives.getCategoryId())) {
            category = categoryService.selectById(archives.getCategoryId());
            formId = category.getFormId();
            //构建路径
            templatePath.append("/" + category.getArticleTemp());
        }
        else {//顶级分类走该模版
            templatePath.append("/article.html");
            category = new Category();
            category.setId("-1");
            category.setCnname("顶级分类");
        }

        try {
            String path = templateDir + templatePath;
            File template = new File(path);
            if (!template.exists()) {
                throw new TemplateNotFoundException(TEMPLATE_NOTFOUND_EXCEPTION,
                                                    "请仔细检查" + path
                                                            + "文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
            }
            
            String html = FileUtils.readFileToString(template, DEFAULT_CHARSET);
            String newHtml = parseEngine.parse(html);
            newHtml = parseEngine.parseCategory(newHtml, category.getCode());
            newHtml = parseEngine.parseArticle(newHtml, id);
            outHtml(newHtml);
        }
        catch (IOException e) {
            throw new TemplateReadException(TEMPLATE_READ_EXCEPTION,
                                            "请仔细检查模版文件，或检查application.yml中的资源目录配置项（web.resource-path）。");
        }

        /* //上一篇下一篇 params = new HashMap<String, Object>(); params.put("arcid",
         * article.get("aid").toString()); params.put("categoryId", category.getId());
         * ArchivesWithRownum currentArticle =
         * archivesService.queryArticleRowNum(params);
         * 
         * params.remove("arcid"); params.put("privNum", (currentArticle.getRownum() -
         * 1)+""); ArchivesWithRownum prevArc =
         * archivesService.queryArticleRowNum(params);
         * 
         * params.remove("privNum"); params.put("nextNum", (currentArticle.getRownum() +
         * 1)+""); ArchivesWithRownum nextArc =
         * archivesService.queryArticleRowNum(params); if(prevArc == null) { prevArc =
         * new ArchivesWithRownum(); prevArc.setTitle("没有了"); } if(nextArc == null) {
         * nextArc = new ArchivesWithRownum(); nextArc.setTitle("没有了"); } */
    }

    @RequestMapping(value = "/search")
    public String search(Model model, SearchEntity params) {
        StringBuffer templatePath = new StringBuffer();
        Theme theme = themeService.getCurrentTheme();
        if (theme == null) {

        }
        model.addAttribute("theme", theme);

        PageInfo<ArchivesVo> pageinfo = archivesService.queryListByKeywords(params);
        model.addAttribute("result", pageinfo);

        String keywords = params.getEntity().get("keywords").toString();

        //记录搜索关键词
        SearchRecord sr = new SearchRecord();
        sr.setId(UUIDUtils.getPrimaryKey());
        sr.setKeywords(keywords);
        sr.setCreateTime(new Date());
        searchRecordService.add(sr);

        model.addAttribute("keywords", keywords);
        templatePath.append("/themes/" + theme.getThemePath() + "/search.html");
        return templatePath.toString();
    }

    /**
     * 取得HttpServletRequest对象
     * 
     * @return HttpServletRequest对象
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 取得Response对象
     * 
     * @return
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * 输出字符串到页面
     * 
     * @param str
     *            字符
     */
    public void outHtml(String html) {
        try {
            HttpServletResponse httpServletResponse = getResponse();
            httpServletResponse.setCharacterEncoding(Constant.DEFAULT_ENCODING);
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.setHeader("Cache-Control", "no-cache");
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0L);
            httpServletResponse.getWriter().write(html);
            httpServletResponse.flushBuffer();
        }
        catch (IOException e) {}
    }
}
