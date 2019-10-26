package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.Field;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.service.ArchivesService;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.FieldService;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtils;

/**
 * Category标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:article /}",endTag="",regexp="(\\{dreamer-cms:article[ \\t]+.*?/\\})|(\\{dreamer-cms:article[ \\t]+.*\\}\\{/dreamer-cms:article\\})", attributes={
		@Attribute(name = "field",regex = "[ \t]+field=[\"\'].*?[\"\']"),
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
	public String parse(String html,String id) {
		Tag annotations = ArticleTag.class.getAnnotation(Tag.class);
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtils.isBlank(all)) {
			return html;
		}
		cn.itechyou.cms.entity.System system = systemService.getSystem();
		String newHtml = html;
		
		Attribute[] attributes = annotations.attributes();
		for (String string : all) {
			Map<String,Object> entity = new HashMap<String,Object>();
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(string, attribute.regex(), 0);
				if(StringUtils.isBlank(condition)) {
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
				if(FieldEnum.FIELD_ID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, archivesVo.get("aid").toString());
				}else if (FieldEnum.FIELD_TITLE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("title")) ? "" : archivesVo.get("title").toString());
				}else if (FieldEnum.FIELD_PROPERTIES.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("properties")) ? "" : archivesVo.get("properties").toString());
				}else if (FieldEnum.FIELD_LITPIC.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getWebsite() + system.getUploaddir() + "/" + imagePath);
				}else if (FieldEnum.FIELD_TAG.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("tag")) ? "" : archivesVo.get("tag").toString());
				}else if (FieldEnum.FIELD_REMARK.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("description")) ? "" : archivesVo.get("description").toString());
				}else if (FieldEnum.FIELD_CATEGORYID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_TYPENAMECN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_TYPENAMEEN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
				}else if (FieldEnum.FIELD_COMMENT.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("comment")) ? "" : archivesVo.get("comment").toString());
				}else if (FieldEnum.FIELD_SUBSCRIBE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("subscribe")) ? "" : archivesVo.get("subscribe").toString());
				}else if (FieldEnum.FIELD_CLICKS.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("clicks")) ? "" : archivesVo.get("clicks").toString());
				}else if (FieldEnum.FIELD_WEIGHT.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("weight")) ? "" : archivesVo.get("weight").toString());
				}else if (FieldEnum.FIELD_STATUS.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("status")) ? "" : archivesVo.get("status").toString());
				}else if (FieldEnum.FIELD_CREATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("createBy")) ? "" : archivesVo.get("createBy").toString());
				}else if (FieldEnum.FIELD_CREATETIME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("createTime")) ? "" : archivesVo.get("createTime").toString());
				}else if (FieldEnum.FIELD_UPDATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("updateBy")) ? "" : archivesVo.get("updateBy").toString());
				}else if (FieldEnum.FIELD_UPDATETIME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get("updateTime")) ? "" : archivesVo.get("updateTime").toString());
				}else if (FieldEnum.FIELD_ARCURL.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, "/article/"+archivesVo.get("aid").toString());
				}else {
					//处理附加字段
					if(archivesVo.containsKey(name))
						newHtml = newHtml.replace(string, StringUtils.isBlank(archivesVo.get(name)) ? "" : archivesVo.get(name).toString());
				}
				
			}
		}
		return newHtml;
	}

}
