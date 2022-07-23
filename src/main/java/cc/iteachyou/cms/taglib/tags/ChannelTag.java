package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * Channel标签解析器
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:channel}",endTag="{/dreamer-cms:channel}",regexp="\\{dreamer-cms:channel[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:channel\\}", attributes={
		@Attribute(name = "typeid",regex = "[ \t]+typeid=\".*?\""),
		@Attribute(name = "start",regex = "[ \t]+start=\".*?\""),
		@Attribute(name = "length",regex = "[ \t]+length=\".*?\""),
		@Attribute(name = "type",regex = "[ \t]+type=\".*?\""),
		@Attribute(name = "showall",regex = "[ \t]+showall=\".*?\"")
	})
public class ChannelTag extends AbstractChannelTag implements IParse {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public String parse(String html) throws CmsException {
		Tag channelAnnotation = ChannelTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, channelAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, channelAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		Attribute[] attributes = channelAnnotation.attributes();
		
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

			List<Category> list = null;
			
			Category category = null;
			String code = StringUtil.isBlank(entity.get("type")) ? "top" : entity.get("type").toString();
			
			String isShow = null;
			if(!entity.containsKey("showall") || StringUtil.isBlank(entity.get("showall"))) {
				isShow = "1";
			}else {
				if(!"true".equalsIgnoreCase(entity.get("showall").toString())) {
					isShow = "1";
				}
			}
			//新的参数实体
			Map<String,Object> newEntity = new HashMap<String,Object>();
			newEntity.put("isShow", isShow);
			
			if("top".equals(code)) {
				category = new Category();
				category.setId("-1");
				newEntity.put("parentId", category.getId());
				if(entity.containsKey("start") && entity.containsKey("length")) {
					newEntity.put("start", entity.get("start").toString());
					newEntity.put("length", entity.get("length").toString());
				}
				list = categoryService.getTreeList(newEntity);
			}else {
				category = categoryService.queryCategoryByCode(entity.get("typeid").toString());
				if(category != null) {
					newEntity.put("parentId", category.getId());
					if(entity.containsKey("start") && entity.containsKey("length")) {
						newEntity.put("start", entity.get("start").toString());
						newEntity.put("length", entity.get("length").toString());
					}
					list = categoryService.getTreeList(newEntity);
				}
			}
			
			StringBuilder sb = new StringBuilder();
			if(list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					Category temp = list.get(j);
					String item = new String(content);
					
					item = buildHTML(item, temp, (j + 1));
					sb.append(item);
				}
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

	@Override
	public String parse(String html,String typeid) throws CmsException {
		Tag channelAnnotation = ChannelTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, channelAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, channelAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		Attribute[] attributes = channelAnnotation.attributes();
		
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
			
			String isShow = StringUtil.isBlank(entity.get("isall")) ? "1" : entity.get("isall").toString();
			//新的参数实体
			Map<String,Object> newEntity = new HashMap<String,Object>();
			newEntity.put("isShow", isShow);
			
			
			if(entity.containsKey("start") && entity.containsKey("length")) {
				newEntity.put("start", entity.get("start").toString());
				newEntity.put("length", entity.get("length").toString());
			}

			List<Category> list = null;
			Category category = null;
			
			category = categoryService.queryCategoryByCode(typeid);
			if(category != null) {
				newEntity.put("parentId", category.getId());
				list = categoryService.getTreeList(newEntity);
			}
			
			StringBuilder sb = new StringBuilder();
            if(list != null){
                for (int j = 0; j < list.size(); j++) {
                    Category temp = list.get(j);
                    String item = new String(content);
                    
                    item = this.buildHTML(item, temp, (j + 1));
                    sb.append(item);
			    }
            }
			
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

}
