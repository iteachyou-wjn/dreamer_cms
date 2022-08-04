package cc.iteachyou.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.FieldService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.LabelService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 文章管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/archives")
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
	
	@Log(operType = OperatorType.PAGE, module = "文章管理", content = "文章分页列表")
	@RequestMapping("/list")
	@RequiresPermissions("e6r77x94")
	public String toIndex(Model model ,SearchEntity params) {
		if(params.getEntity() == null) {
			Map<String,Object> entity = new HashMap<String,Object>();
			params.setEntity(entity);
		}
		String cid = "-1";
		if(params.getEntity().containsKey("cid")) {
			cid = params.getEntity().get("cid").toString();
			if("-1".equals(params.getEntity().get("cid").toString())) {
				params.getEntity().remove("cid");
			}
		}
		
		if(params.getEntity().containsKey("cid")) {
			cid = params.getEntity().get("cid").toString();
			Category category = categoryService.queryCategoryByCode(cid);
			model.addAttribute("category", category);
		}
		params.getEntity().put("sortBy", "create_time");
		params.getEntity().put("sortWay", "desc");
		PageInfo<Map<String,Object>> archives = archivesService.queryListByPage(params);
		model.addAttribute("cid", cid == null ? "" : cid);
		model.addAttribute("page", archives);
		return "admin/archives/list";
	}
	
	@Log(operType = OperatorType.OTHER, module = "文章管理", content = "添加文章页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("a7f3sqap")
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
	
	/**
	 * 添加文章
	 * @return
	 * @throws CmsException
	 */
	@Log(operType = OperatorType.INSERT, module = "文章管理", content = "添加文章")
	@RequestMapping("/add")
	@RequiresPermissions("0d2132i8")
	public String add(Model model,HttpServletRequest request,@RequestParam Map<String,String> entity) throws CmsException {
		Archives archives = new Archives();
		archives.setId(UUIDUtils.getPrimaryKey());
		archives.setCreateTime(new Date());
		archives.setCreateBy(TokenManager.getToken().getId());
		archives.setStatus(1);//未发布
		archives.setTitle(entity.get("title"));
		archives.setTag(entity.get("tag"));
		archives.setCategoryId(entity.get("categoryId"));
		archives.setCategoryIds(entity.get("categoryIds"));
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
			if(StringUtil.isNotBlank(entity.get(field.getFieldName()))){
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
		}
		String tableName = "system_" + form.getTableName();
		try {
			archivesService.save(archives,tableName,additional);
		} catch (TransactionException e) {
			e.printStackTrace();
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/archives/list?entity%5Bcid%5D=" + categoryCode;
	}
	
	@Log(operType = OperatorType.OTHER, module = "文章管理", content = "修改文章页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("lk7s7t2n")
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
	
	@Log(operType = OperatorType.UPDATE, module = "文章管理", content = "修改文章")
	@RequestMapping(value ="/edit")
	@RequiresPermissions("th018nx3")
	public String edit(Model model,HttpServletRequest request,@RequestParam Map<String,String> entity) throws CmsException {
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
			if(StringUtil.isNotBlank(entity.get(field.getFieldName()))){
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
		}
		String tableName = "system_" + form.getTableName();
		
		try {
			archivesService.update(archives, tableName, additional, entity.get("fid"));
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/archives/list?entity%5Bcid%5D=" + categoryCode;
	}
	
	@Log(operType = OperatorType.DELETE, module = "文章管理", content = "删除文章")
	@RequestMapping(value ="/delete")
	@RequiresPermissions("n4lyn017")
	public String delete(Model model, String id,String cid) throws CmsException {
		Map<String,Object> params = new HashMap<String,Object>();
		String categoryCode = "";
		if(!"-1".equals(cid)) {
			Category category = categoryService.selectById(cid);
			categoryCode = category.getCode();
			Form form = formService.queryFormById(category.getFormId());
			params.put("tableName", "system_" + form.getTableName());
		}else {
			params.put("tableName", "system_article");
		}
		params.put("id", id);
		Map<String,Object> article = archivesService.queryArticleById(params);
		//对原有标签进行删除
		if(article.get("tag") != null) {
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
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/archives/list?entity%5Bcid%5D=" + categoryCode;
	}
}
