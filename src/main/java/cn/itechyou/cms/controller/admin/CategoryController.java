package cn.itechyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.Json;
import cn.itechyou.cms.common.Result;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.StringUtils;
import cn.itechyou.cms.utils.UUIDUtils;

@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private SystemService systemService;

    @RequestMapping("/list")
    public String list(Model model, SearchEntity params) {
        PageInfo<CategoryWithBLOBs> page = categoryService.queryListByPage(params);
        model.addAttribute("categorys", page);
        return "admin/category/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model, String id) {
        Category category = null;
        if (id.equals("-1")) {
            category = new Category();
            category.setParentId("-1");
            category.setLevel("1");
            category.setCnname("顶级栏目");
        }
        else {
            category = categoryService.selectById(id);
            category.setLevel((Integer.parseInt(category.getLevel()) + 1) + "");
        }

        List<Form> forms = formService.queryAll();

        model.addAttribute("category", category);
        model.addAttribute("forms", forms);
        return "admin/category/add";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Model model, String id) {
        CategoryWithBLOBs category = categoryService.selectById(id);
        if (category.getParentId().equals("-1")) {
            category.setParentName("顶级栏目");
        }
        else {
            Category category2 = categoryService.selectById(category.getParentId());
            category.setParentName(category2.getCnname());
        }

        List<Form> forms = formService.queryAll();
        System system = systemService.getSystem();
        model.addAttribute("category", category);
        model.addAttribute("forms", forms);
        model.addAttribute("system", system);
        return "admin/category/edit";
    }

    @RequestMapping("/add")
    public String add(CategoryWithBLOBs category) {
        category.setId(UUIDUtils.getPrimaryKey());
        category.setCode(UUIDUtils.getCharAndNumr(8));
        category.setLevel(category.getParentId().equals("-1") ? "1" : category.getLevel());
        category.setParentId(StringUtils.isBlank(category.getParentId()) ? "-1" : category.getParentId());
        category.setCreateBy(TokenManager.getToken().getId());
        category.setCreateTime(new Date());
        if (!"-1".equals(category.getParentId())) {
            Category parent = categoryService.selectById(category.getParentId());
            category.setCatSeq(parent.getCatSeq() + "." + category.getCode());
        }
        else {
            category.setCatSeq("." + category.getCode());
        }

        //处理模版
        if (StringUtils.isNotBlank(category.getCoverTemp()) && !category.getCoverTemp().startsWith("/")) {
            category.setCoverTemp("/" + category.getCoverTemp());
        }
        if (StringUtils.isNotBlank(category.getListTemp()) && !category.getListTemp().startsWith("/")) {
            category.setListTemp("/" + category.getListTemp());
        }
        if (StringUtils.isNotBlank(category.getArticleTemp()) && !category.getArticleTemp().startsWith("/")) {
            category.setArticleTemp("/" + category.getArticleTemp());
        }
        categoryService.save(category);
        return "redirect:/admin/category/list";
    }

    @RequestMapping(value = "/edit")
    public String edit(CategoryWithBLOBs category) {
        category.setUpdateBy(TokenManager.getToken().getId());
        category.setUpdateTime(new Date());
        //处理模版
        if (StringUtils.isNotBlank(category.getCoverTemp()) && !category.getCoverTemp().startsWith("/")) {
            category.setCoverTemp("/" + category.getCoverTemp());
        }
        if (StringUtils.isNotBlank(category.getListTemp()) && !category.getListTemp().startsWith("/")) {
            category.setListTemp("/" + category.getListTemp());
        }
        if (StringUtils.isNotBlank(category.getArticleTemp()) && !category.getArticleTemp().startsWith("/")) {
            category.setArticleTemp("/" + category.getArticleTemp());
        }
        categoryService.update(category);
        return "redirect:/admin/category/list";
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        categoryService.delete(id);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/loadSon")
    public Result loadSon(String id) {
        List<CategoryWithBLOBs> list = categoryService.selectByParentId(id);
        return Json.ok(list);
    }

    @ResponseBody
    @RequestMapping("/updateSort")
    public void updateSort(@RequestBody List<Category> list) {
        categoryService.updateSort(list);
    }
}
