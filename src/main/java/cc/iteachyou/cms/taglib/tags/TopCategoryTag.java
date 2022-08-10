package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.enums.FieldEnum;
import cc.iteachyou.cms.taglib.utils.FunctionUtil;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.taglib.utils.URLUtils;
import cc.iteachyou.cms.utils.PinyinUtils;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * Category标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:topcategory /}",endTag="",regexp="(\\{dreamer-cms:topcategory[ \\t]+.*?/\\})|(\\{dreamer-cms:topcategory[ \\t]+.*\\}\\{/dreamer-cms:topcategory\\})", attributes={
	@Attribute(name = "field",regex = "[ \t]+field=[\"\'].*?[\"\']"),
	@Attribute(name = "function",regex = "[ \t]+function=\\\"((.*)\\((.*?)\\)?)\\\""),
})
public class TopCategoryTag implements IParse {

	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryService categoryService;
	
	private String t;
	
	@Override
	public String parse(String html) {
		return null;
	}
	
	@Override
	public String parse(String html, String typeid) throws CmsException {
		Tag annotations = TopCategoryTag.class.getAnnotation(Tag.class);
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
			
			Category category = categoryService.queryCategoryByCode(typeid);
			
			String catSeq = category.getCatSeq();
			catSeq = catSeq.substring(1);
			String[] categoryCodes = catSeq.split("\\.");
			
			Category temp = null;
			if(category.getCode().equals(categoryCodes[0])) {
				temp = category;
			}else {
				temp = categoryService.queryCategoryByCode(categoryCodes[0]);
			}
			
			String imagePath = "";
			if(StringUtil.isNotBlank(temp.getImagePath())) {
				imagePath = temp.getImagePath();
				imagePath = imagePath.replace("\\", "/");
			}
			String typeCode = StringUtil.isBlank(temp.getCode()) ? "" : temp.getCode();
			String visitUrl = StringUtil.isBlank(temp.getVisitUrl()) ? PinyinUtils.toPinyin(temp.getCnname()) : temp.getVisitUrl();
			if(!visitUrl.startsWith("/")) {
				visitUrl = "/" + visitUrl;
			}
			
			if(entity.containsKey("field")) {
				String name = entity.get("field").toString();
				String function = StringUtil.isBlank(entity.get("function")) ? "" : entity.get("function").toString();
				if(FieldEnum.FIELD_TYPEID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, temp.getId());
				}else if (FieldEnum.FIELD_TYPENAMECN.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, temp.getCnname()));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCnname()) ? "" : temp.getCnname());
					}
				}else if (FieldEnum.FIELD_TYPENAMEEN.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, temp.getEnname()));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getEnname()) ? "" : temp.getEnname());
					}
				}else if (FieldEnum.FIELD_TYPECODE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, typeCode);
				}else if (FieldEnum.FIELD_TYPESEQ.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCatSeq()) ? "" : temp.getCatSeq());
				}else if (FieldEnum.FIELD_TYPEIMG.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getWebsite() + Constant.UPLOAD_PREFIX + system.getUploaddir() + "/" + imagePath);
				}else if (FieldEnum.FIELD_DESCRIPTION.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getDescription()) ? "" : temp.getDescription());
				}else if (FieldEnum.FIELD_LINKTARGET.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getLinkTarget()) ? "" : temp.getLinkTarget());
				}else if (FieldEnum.FIELD_PAGESIZE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : temp.getPageSize().toString());
				}else if (FieldEnum.FIELD_VISITURL.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, visitUrl);
				}else if (FieldEnum.FIELD_LINKURL.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getLinkUrl()) ? "" : temp.getLinkUrl());
				}else if (FieldEnum.FIELD_EDITOR.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getDefaultEditor()) ? "" : temp.getDefaultEditor());
				}else if (FieldEnum.FIELD_MARKDOWN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getMdContent()) ? "" : temp.getMdContent());
				}else if (FieldEnum.FIELD_UEHTML.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getHtmlContent()) ? "" : temp.getHtmlContent());
				}else if (FieldEnum.FIELD_PARENTID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getParentId()) ? "" : temp.getParentId());
				}else if (FieldEnum.FIELD_PARENTNAME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getParentName()) ? "" : temp.getParentName());
				}else if (FieldEnum.FIELD_ISSHOW.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getIsShow()) ? "" : temp.getIsShow().toString());
				}else if (FieldEnum.FIELD_LEVEL.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getLevel()) ? "" : temp.getLevel());
				}else if (FieldEnum.FIELD_SORT.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getSort()) ? "" : temp.getSort().toString());
				}else if (FieldEnum.FIELD_TCREATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCreateBy()) ? "" : temp.getCreateBy());
				}else if (FieldEnum.FIELD_TCREATETIME.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, temp.getCreateTime()));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCreateTime()) ? "" : temp.getCreateTime().toString());
					}
				}else if (FieldEnum.FIELD_TUPDATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getUpdateBy()) ? "" : temp.getUpdateBy());
				}else if (FieldEnum.FIELD_TUPDATETIME.getField().equalsIgnoreCase(name)) {
					if(StringUtil.isNotBlank(function)) {
						newHtml = newHtml.replace(string, FunctionUtil.replaceByFunction(function, temp.getUpdateTime()));
					}else {
						newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getUpdateTime()) ? "" : temp.getUpdateTime().toString());
					}
				}else if (FieldEnum.FIELD_TYPEURL.getField().equalsIgnoreCase(name)) {
					String url = URLUtils.parseURL(system, temp, this.t);
					newHtml = newHtml.replace(string, url);
				}else if (FieldEnum.FIELD_EXT01.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt01()) ? "" : temp.getExt01());
				}else if (FieldEnum.FIELD_EXT02.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt02()) ? "" : temp.getExt02());
				}else if (FieldEnum.FIELD_EXT03.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt03()) ? "" : temp.getExt03());
				}else if (FieldEnum.FIELD_EXT04.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt04()) ? "" : temp.getExt04());
				}else if (FieldEnum.FIELD_EXT05.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt05()) ? "" : temp.getExt05());
				}else if (FieldEnum.FIELD_EXT05.getField().equalsIgnoreCase(name)) {//顶级栏目
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt05()) ? "" : temp.getExt05());
				}else if (FieldEnum.FIELD_EXT05.getField().equalsIgnoreCase(name)) {//顶级栏目
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getExt05()) ? "" : temp.getExt05());
				}
			}
		}
		return newHtml;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

}
