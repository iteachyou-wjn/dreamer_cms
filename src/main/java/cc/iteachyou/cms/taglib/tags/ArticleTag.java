package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.FieldService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.enums.FieldEnum;
import cc.iteachyou.cms.taglib.utils.FunctionUtil;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * Category标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:article /}",endTag="",regexp="(\\{dreamer-cms:article[ \\t]+.*?/\\})|(\\{dreamer-cms:article[ \\t]+.*\\}\\{/dreamer-cms:article\\})", attributes={
		@Attribute(name = "field",regex = "[ \t]+field=[\"\'].*?[\"\']"),
		@Attribute(name = "function",regex = "[ \t]+function=\\\"((.*)\\((.*?)\\)?)\\\""),
	})
public class ArticleTag implements IParse {

	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private FormService formService;
	@Autowired
	private FieldService fieldService;
	
	@Override
	public String parse(String html) {
		return null;
	}
	
	@Override
	public String parse(String html,String id) throws CmsException {
		Tag annotations = ArticleTag.class.getAnnotation(Tag.class);
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(all)) {
			return html;
		}
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		String newHtml = html;
		
		Attribute[] attributes = annotations.attributes();
		for (String string : all) {
			Map<String,Object> entity = new HashMap<String,Object>();
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(string, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			
			Archives archives = archivesService.selectByPrimaryKey(id);
			String formId = formService.queryDefaultForm().getId();
			Category category = null;
			if(!"-1".equals(archives.getCategoryId())) {
				category = categoryService.selectById(archives.getCategoryId());
				formId = category.getFormId();
			}else {//顶级分类走该模版
				category = new Category();
				category.setId("-1");
				category.setCnname("顶级分类");
			}
			
			Form form = formService.queryFormById(formId);
			List<Field> fields = fieldService.queryFieldByFormId(formId);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("tableName", "system_" + form.getTableName());
			params.put("id", archives.getId());
			
			Map<String, Object> archivesVo = archivesService.queryArticleById(params);
			
			String imagePath = "";
			if(archivesVo.containsKey("imagePath")) {
				imagePath = archivesVo.get("imagePath").toString();
				imagePath = imagePath.replace("\\", "/");
			}
			if(entity.containsKey("field")) {
				String name = entity.get("field").toString();
				String function = StringUtil.isBlank(entity.get("function")) ? "" : entity.get("function").toString();
				if(FieldEnum.FIELD_ID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, archivesVo.get("aid").toString());
				}else if (FieldEnum.FIELD_TITLE.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, archivesVo.get("title")));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("title")) ? "" : archivesVo.get("title").toString());
					}
				}else if (FieldEnum.FIELD_PROPERTIES.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("properties")) ? "" : archivesVo.get("properties").toString());
				}else if (FieldEnum.FIELD_LITPIC.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getWebsite() + system.getUploaddir() + "/" + imagePath);
				}else if (FieldEnum.FIELD_TAG.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("tag")) ? "" : archivesVo.get("tag").toString());
				}else if (FieldEnum.FIELD_REMARK.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("description")) ? "" : archivesVo.get("description").toString());
				}else if (FieldEnum.FIELD_CATEGORYID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_TYPENAMECN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_TYPENAMEEN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_COMMENT.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("comment")) ? "" : archivesVo.get("comment").toString());
				}else if (FieldEnum.FIELD_SUBSCRIBE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("subscribe")) ? "" : archivesVo.get("subscribe").toString());
				}else if (FieldEnum.FIELD_CLICKS.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("clicks")) ? "" : archivesVo.get("clicks").toString());
				}else if (FieldEnum.FIELD_WEIGHT.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("weight")) ? "" : archivesVo.get("weight").toString());
				}else if (FieldEnum.FIELD_STATUS.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("status")) ? "" : archivesVo.get("status").toString());
				}else if (FieldEnum.FIELD_CREATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("createBy")) ? "" : archivesVo.get("createBy").toString());
				}else if (FieldEnum.FIELD_CREATEUSERNAME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("createUserName")) ? "" : archivesVo.get("createUserName").toString());
				}else if (FieldEnum.FIELD_CREATEREALNAME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("createRealName")) ? "" : archivesVo.get("createRealName").toString());
				}else if (FieldEnum.FIELD_CREATETIME.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, archivesVo.get("createTime")));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("createTime")) ? "" : archivesVo.get("createTime").toString());
					}
				}else if (FieldEnum.FIELD_UPDATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("updateBy")) ? "" : archivesVo.get("updateBy").toString());
				}else if (FieldEnum.FIELD_UPDATETIME.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, archivesVo.get("updateTime")));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get("updateTime")) ? "" : archivesVo.get("updateTime").toString());
					}
				}else if (FieldEnum.FIELD_ARCURL.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, "/article/"+archivesVo.get("aid").toString());
				}else {
					//处理附加字段
					if(archivesVo.containsKey(name))
						newHtml = newHtml.replace(string, StringUtil.isBlank(archivesVo.get(name)) ? "" : archivesVo.get(name).toString());
				}
				
			}
		}
		return newHtml;
	}
	
}
