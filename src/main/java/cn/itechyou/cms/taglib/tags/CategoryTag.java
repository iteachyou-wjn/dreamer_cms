package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtil;

/**
 * Category标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:category /}",endTag="",regexp="(\\{dreamer-cms:category[ \\t]+.*?/\\})|(\\{dreamer-cms:category[ \\t]+.*\\}\\{/dreamer-cms:category\\})", attributes={
		@Attribute(name = "field",regex = "[ \t]+field=[\"\'].*?[\"\']"),
	})
public class CategoryTag implements IParse {

	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public String parse(String html) {
		return null;
	}
	
	@Override
	public String parse(String html,String typeid) {
		Tag annotations = CategoryTag.class.getAnnotation(Tag.class);
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(all)) {
			return html;
		}
		cn.itechyou.cms.entity.System system = systemService.getSystem();
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
			
			CategoryWithBLOBs temp = categoryService.queryCategoryByCode(typeid);
			
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
				if(FieldEnum.FIELD_TYPEID.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, temp.getId());
				}else if (FieldEnum.FIELD_TYPENAMECN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCnname()) ? "" : temp.getCnname());
				}else if (FieldEnum.FIELD_TYPENAMEEN.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getEnname()) ? "" : temp.getEnname());
				}else if (FieldEnum.FIELD_TYPECODE.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, typeCode);
				}else if (FieldEnum.FIELD_TYPESEQ.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCatSeq()) ? "" : temp.getCatSeq());
				}else if (FieldEnum.FIELD_TYPEIMG.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getWebsite() + system.getUploaddir() + "/" + imagePath);
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
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getCreateTime()) ? "" : temp.getCreateTime().toString());
				}else if (FieldEnum.FIELD_TUPDATEBY.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getUpdateBy()) ? "" : temp.getUpdateBy());
				}else if (FieldEnum.FIELD_TUPDATETIME.getField().equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getUpdateTime()) ? "" : temp.getUpdateTime().toString());
				}else if (FieldEnum.FIELD_TYPEURL.getField().equalsIgnoreCase(name)) {
					if(temp.getCatModel() == 1) {
						newHtml = newHtml.replace(string, "/cover-" + typeCode + visitUrl);
					}else if(temp.getCatModel() == 2) {
						newHtml = newHtml.replace(string, "/list-" + typeCode + visitUrl + "/1/"
								+ (StringUtil.isBlank(temp.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : temp.getPageSize().toString()));
					}else if(temp.getCatModel() == 3) {
						newHtml = newHtml.replace(string, StringUtil.isBlank(temp.getLinkUrl()) ? "" : temp.getLinkUrl());
					}
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
				}
			}
		}
		return newHtml;
	}

}
