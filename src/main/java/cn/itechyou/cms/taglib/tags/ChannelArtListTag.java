package cn.itechyou.cms.taglib.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.exception.CategoryNotFoundException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.StringUtil;

/**
 * ChannelArtList标签解析器
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:categoryartlist}",endTag="{/dreamer-cms:categoryartlist}",regexp="\\{dreamer-cms:categoryartlist.*\\}([\\s\\S]+?)\\{/dreamer-cms:categoryartlist\\}",attributes={
	@Attribute(name = "typeid",regex = "[ \t]+typeid=[\"\'].*?[\"\']"),
	@Attribute(name = "length",regex = "[ \t]+length=[\"\'].*?[\"\']"),
	@Attribute(name = "showall",regex = "[ \t]+showall=\".*?\"")
})
public class ChannelArtListTag extends AbstractChannelTag implements IParse {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ListTag listTag;
	@Autowired ChannelTag channelTag;
	
	@Override
	public String parse(String html) throws CmsException {
		Tag annotations = ChannelArtListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, annotations.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, annotations.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		Attribute[] attributes = annotations.attributes();
		
		String newHtml = html;
		
		for (int i = 0;i < listTags.size();i++) {
			String tag = listTags.get(i);
			String content = contents.get(i);
			
			Map<String,Object> entity = new HashMap<String,Object>();
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(tag, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			
			Category category = null;
			List<Category> categorys = new ArrayList<Category>();
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			//新的参数实体
			Map<String,Object> newEntity = new HashMap<String,Object>();
			newEntity.put("start", 0);
			newEntity.put("isShow", 1);

			String isShow = null;
			if(!entity.containsKey("showall") || StringUtil.isBlank(entity.get("showall"))) {
				isShow = "1";
			}else {
				if(!"true".equalsIgnoreCase(entity.get("showall").toString())) {
					isShow = "1";
				}
			}
			newEntity.put("isShow", isShow);

			if(!entity.containsKey("typeid")) {
				category = new Category();
				category.setId("-1");
				newEntity.put("parentId", category.getId());
				if(entity.containsKey("length")) {
					newEntity.put("length", entity.get("length").toString());
				}
				categorys = categoryService.getTreeList(newEntity);
			}else {
				String code = entity.get("typeid").toString();
				entity.remove("typeid");
				
				category = categoryService.queryCategoryByCode(code);
				if(category == null) {
					throw new CategoryNotFoundException(
							ExceptionEnum.CAT_NOTFOUND_EXCEPTION.getCode(), 
							ExceptionEnum.CAT_NOTFOUND_EXCEPTION.getMessage(),
							"栏目不存在，请检查标签中是否调用了不存在的（typeid）。");
				}
				newEntity.put("parentId", category.getId());
				if(entity.containsKey("length")) {
					newEntity.put("start", 0);
					newEntity.put("length", entity.get("length").toString());
				}
				categorys = categoryService.getTreeList(newEntity);
			}
			StringBuilder sb = new StringBuilder();
			if(categorys == null || categorys.size() <= 0) {
				String item = new String(content);
				if(StringUtil.isNotBlank(this.getT())) {
					channelTag.setT(this.getT());
				}
				if(StringUtil.isNotBlank(this.getT())) {
					listTag.setT(this.getT());
				}
				item = listTag.parse(item, category.getCode());
				item = channelTag.parse(item, category.getCode());
				item = this.buildHTML(item, category, 1);
				sb.append(item);
			}else {
				for (int j = 0; j < categorys.size(); j++) {
					Category categoryWithBLOBs = categorys.get(j);
					String item = new String(content);
					if(StringUtil.isNotBlank(this.getT())) {
						channelTag.setT(this.getT());
					}
					if(StringUtil.isNotBlank(this.getT())) {
						listTag.setT(this.getT());
					}
					item = listTag.parse(item, categoryWithBLOBs.getCode());
					item = channelTag.parse(item, categoryWithBLOBs.getCode());
					item = this.buildHTML(item,categoryWithBLOBs, (j + 1));
					sb.append(item);
				}
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}