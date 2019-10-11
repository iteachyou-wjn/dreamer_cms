package cn.itechyou.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.Field;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.exception.TransactionException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.FieldService;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.LabelService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.vo.ArchivesVo;

@Controller
@RequestMapping("/admin/archives")
public class ArchivesController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private LabelService labelService;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private FormService formService;
	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/list")
	public String toIndex(Model model ,SearchEntity params) {
		Map<String,Object> map = new HashMap<>();
		String cid = "-1";
		if(params.getEntity() != null) {
			cid = params.getEntity().containsKey("cid") ? params.getEntity().get("cid").toString() : "-1";
		}
		PageInfo<Map<String,Object>> archives = archivesService.queryListByPage(params);
		model.addAttribute("cid", cid == null ? "-1" : cid);
		model.addAttribute("archives", archives);
		return "admin/archives/list";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(Model model, String code) {
		Category category = null;
		Form form = new Form();
		if(code.equals("-1")) {
			category = new Category();
			category.setCnname("顶级栏目");
			category.setId("-1");
			form = formService.queryDefaultForm();
		}else {
			category = categoryService.queryCategoryByCode(code);
			form.setId(category.getFormId());
		}
		List<Field> fields = fieldService.queryFieldByFormId(form.getId());
		model.addAttribute("category", category);
		model.addAttribute("fields", fields);
		return "admin/archives/add";
	}
	
	@RequestMapping("/add")
	public String add(Model model,HttpServletRequest request,@RequestParam Map<String,String> entity) {
		Archives archives = new Archives();
		archives.setId(UUIDUtils.getPrimaryKey());
		archives.setCreateTime(new Date());
		archives.setCreateBy(TokenManager.getToken().getId());
		archives.setStatus(1);//未发布
		archives.setTitle(entity.get("title"));
		archives.setTag(entity.get("tag"));
		archives.setCategoryId(entity.get("categoryId"));
		archives.setImagePath(entity.get("imagePath"));
		archives.setWeight(StringUtil.isBlank(entity.get("weight")) ? 0 : Integer.parseInt(entity.get("weight")));
		archives.setClicks(StringUtil.isBlank(entity.get("clicks")) ? 0 : Integer.parseInt(entity.get("clicks")));
		archives.setDescription(entity.get("description"));
		archives.setComment(Integer.parseInt(entity.get("comment")));
		archives.setSubscribe(Integer.parseInt(entity.get("subscribe")));
		//处理标签
		String[] tags = request.getParameterValues("tag");
		StringBuffer tagStr = new StringBuffer();
		if(tags != null && tags.length > 0) {
			for (String string : tags) {
				tagStr.append(string + ",");
			}
			archives.setTag(tagStr.substring(0, tagStr.length() - 1).toString());
		}
		//处理属性
		String[] propertiesArr = request.getParameterValues("properties");
		StringBuffer properties = new StringBuffer();
		if(propertiesArr != null && propertiesArr.length > 0) {
			for (String string : propertiesArr) {
				properties.append(string + ",");
			}
			archives.setProperties(properties.subSequence(0, properties.length() - 1).toString());
		}else {
			archives.setProperties(StringUtil.isNotBlank(archives.getImagePath()) ? "p" : "n");
		}
		
		//处理标签
		String tag = archives.getTag();
		if(StringUtil.isNotBlank(tag)) {
			String[] tagArr = tag.split(",");
			labelService.insertOrUpdate(tagArr);
		}
		
		if(StringUtil.isNotBlank(archives.getImagePath())) {
			if(StringUtil.isBlank(archives.getProperties())) {
				archives.setProperties("p");
			}else if(!archives.getProperties().contains("p")) {
				archives.setProperties(archives.getProperties() + ",p");
			}
		}
		
		//
		String formId = formService.queryDefaultForm().getId();
		String categoryCode = "";
		if(!"-1".equals(entity.get("categoryId"))) {
			Category category = categoryService.selectById(archives.getCategoryId());
			categoryCode = category.getCode();
			formId = category.getFormId();
		}
		Form form = formService.queryFormById(formId);
		List<Field> fields = fieldService.queryFieldByFormId(formId);
		Map<String,Object> additional = new LinkedHashMap<String,Object>();
		additional.put("id", UUIDUtils.getPrimaryKey());
		additional.put("aid", archives.getId());
		for(int i = 0;i < fields.size();i++) {
			Field field = fields.get(i);
			additional.put(field.getFieldName(), entity.get(field.getFieldName()));
			//用MAP接收参数，checkbox需要特殊处理
			if("checkbox".equals(field.getDataType())) {
				String[] arr = request.getParameterValues(field.getFieldName());
				if(arr != null && arr.length > 0) {
					StringBuffer checkboxVal = new StringBuffer();
					for (String string : arr) {
						checkboxVal.append(string + ",");
					}
					additional.put(field.getFieldName(), checkboxVal.substring(0, checkboxVal.length() - 1));
				}
			}
		}
		String tableName = "system_" + form.getTableName();
		try {
			archivesService.save(archives,tableName,additional);
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/archives/list?entity%5Bcid%5D=" + categoryCode;
	}
	
	
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	public String toEdit(Model model, String id,String cid) {
		String formId = formService.queryDefaultForm().getId();
		if(!"-1".equals(cid)) {
			Category category = categoryService.selectById(cid);
			formId = category.getFormId();
		}
		Form form = formService.queryFormById(formId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("tableName", "system_" + form.getTableName());
		params.put("id", id);
		Map<String,Object> article = archivesService.queryArticleById(params);
		List<Field> fields = fieldService.queryFieldByFormId(form.getId());
		if("-1".equals(article.get("categoryId"))) {
			article.put("categoryCnName", "顶级栏目");
		}
		
		System system = systemService.getSystem();
		model.addAttribute("article", article);
		model.addAttribute("fields", fields);
		model.addAttribute("system", system);
		return "admin/archives/edit";
	}
	
	@RequestMapping(value ="/edit")
	public String edit(Model model,HttpServletRequest request,@RequestParam Map<String,String> entity) {
		Archives archives = new Archives();
		archives.setId(entity.get("id"));
		archives.setTitle(entity.get("title"));
		archives.setCategoryId(entity.get("categoryId"));
		archives.setImagePath(entity.get("imagePath"));
		archives.setWeight(StringUtil.isBlank(entity.get("weight")) ? 0 : Integer.parseInt(entity.get("weight")));
		archives.setClicks(StringUtil.isBlank(entity.get("clicks")) ? 0 : Integer.parseInt(entity.get("clicks")));
		archives.setDescription(entity.get("description"));
		archives.setComment(Integer.parseInt(entity.get("comment")));
		archives.setSubscribe(Integer.parseInt(entity.get("subscribe")));
		archives.setUpdateBy(TokenManager.getToken().getId());
		archives.setUpdateTime(new Date());
		//处理标签
		String[] tags = request.getParameterValues("tag");
		StringBuffer tagStr = new StringBuffer();
		if(tags != null && tags.length > 0) {
			for (String string : tags) {
				tagStr.append(string + ",");
			}
			archives.setTag(tagStr.substring(0, tagStr.length() - 1).toString());
		}
		//处理属性
		String[] propertiesArr = request.getParameterValues("properties");
		StringBuffer properties = new StringBuffer();
		if(propertiesArr != null && propertiesArr.length > 0) {
			for (String string : propertiesArr) {
				properties.append(string + ",");
			}
			archives.setProperties(properties.substring(0, properties.length() - 1).toString());
		}else {
			archives.setProperties(StringUtil.isNotBlank(archives.getImagePath()) ? "p" : "n");
		}
		
		String formId = formService.queryDefaultForm().getId();
		String categoryCode = "";
		if(!"-1".equals(entity.get("categoryId"))) {
			Category category = categoryService.selectById(archives.getCategoryId());
			categoryCode = category.getCode();
			formId = category.getFormId();
		}
		Form form = formService.queryFormById(formId);
		List<Field> fields = fieldService.queryFieldByFormId(formId);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("tableName", "system_" + form.getTableName());
		params.put("id", archives.getId());
		Map<String,Object> oldArchives = archivesService.queryArticleById(params);
		
		//对原有标签进行删除
		String tag = "";
		if(oldArchives.get("tag") != null) {
			tag = oldArchives.get("tag").toString();
			if(StringUtil.isNotBlank(tag)) {
				String[] tagArr = tag.split(",");
				labelService.updateCount(tagArr);
			}
		}
		//处理新标签
		tag = archives.getTag();
		if(StringUtil.isNotBlank(tag)) {
			String[] tagArr = tag.split(",");
			labelService.insertOrUpdate(tagArr);
		}

		Map<String,Object> additional = new LinkedHashMap<String,Object>();
		for(int i = 0;i < fields.size();i++) {
			Field field = fields.get(i);
			additional.put(field.getFieldName(), entity.get(field.getFieldName()));
			//用MAP接收参数，checkbox需要特殊处理
			if("checkbox".equals(field.getDataType())) {
				String[] arr = request.getParameterValues(field.getFieldName());
				if(arr != null && arr.length > 0) {
					StringBuffer checkboxVal = new StringBuffer();
					for (String string : arr) {
						checkboxVal.append(string + ",");
					}
					additional.put(field.getFieldName(), checkboxVal.substring(0, checkboxVal.length() - 1));
				}
			}
		}
		String tableName = "system_" + form.getTableName();
		
		try {
			archivesService.update(archives,tableName,additional,entity.get("fid"));
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/archives/list?entity%5Bcid%5D=" + categoryCode;
	}
	
	@RequestMapping(value ="/delete")
	public String delete(Model model, String id,String cid) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(!"-1".equals(cid)) {
			Category category = categoryService.selectById(cid);
			Form form = formService.queryFormById(category.getFormId());
			params.put("tableName", "system_" + form.getTableName());
		}else {
			params.put("tableName", "system_article");
		}
		params.put("id", id);
		Map<String,Object> article = archivesService.queryArticleById(params);
		//对原有标签进行删除
		if(StringUtil.isNotBlank(article.get("tag"))) {
			String tag = article.get("tag").toString();
			if(StringUtil.isNotBlank(tag)) {
				String[] tagArr = tag.split(",");
				labelService.updateCount(tagArr);
			}
		}
		params.put("id", article.get("id").toString());
		try {
			archivesService.delete(id,params);
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/archives/list";
	}
}
