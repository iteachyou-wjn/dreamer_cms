package cn.itechyou.blog.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.blog.common.Constant;
import cn.itechyou.blog.entity.CategoryWithBLOBs;
import cn.itechyou.blog.service.CategoryService;
import cn.itechyou.blog.service.ThemeService;
import cn.itechyou.blog.taglib.IParse;
import cn.itechyou.blog.taglib.annotation.Attribute;
import cn.itechyou.blog.taglib.annotation.Tag;
import cn.itechyou.blog.taglib.enums.FieldEnum;
import cn.itechyou.blog.taglib.utils.RegexUtil;
import cn.itechyou.blog.utils.StringUtil;

/**
 * Include标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:if}",endTag="{/dreamer-cms:if}",regexp="\\{dreamer-cms:if[ \\t]+.*\\}([\\s\\S]+?)\\{/dreamer-cms:if\\}", attributes={
		@Attribute(name = "test",regex = "[ \t]+test=[\"\']\\(.*?\\)[\"\']"),
	})
public class IfTag implements IParse {
	
	@Override
	public String parse(String html) {
		Tag annotations = IfTag.class.getAnnotation(Tag.class);
		Attribute[] attributes = annotations.attributes();
		List<String> tags = RegexUtil.parseAll(html, annotations.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, annotations.regexp(), 1);
		if(StringUtil.isBlank(tags)) {
			return html;
		}
		String newHtml = html;
		
		for (int i = 0; i < tags.size(); i++) {
			Map<String,Object> entity = new HashMap<String,Object>();
			String tag = tags.get(i);
			String content = contents.get(i);
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
			
			if(!entity.containsKey("test")) {
				return newHtml;
			}
			
			String value = entity.get("test").toString();
			String expression = RegexUtil.parseFirst(value, "(?<=[\\(]).*[ \\t]+(eq|neq)[ \\t]+.*(?=[\\)])", 0);
			String operation = RegexUtil.parseFirst(value, "(?<=[\\(]).*[ \\t]+(eq|neq)[ \\t]+.*(?=[\\)])", 1);
			
			String[] values = expression.split(operation);
			String value1 = values[0];
			String value2 = values[1];
			
			if(StringUtil.isBlank(value1) || StringUtil.isBlank(value2)) {
				//
			}
			switch (operation) {
			case "eq":
				if(!value1.trim().equals(value2.trim())) {
					newHtml = newHtml.replace(tag, "");
				}else {
					newHtml = newHtml.replace(tag, content);
				}
				break;
			case "neq":
				if(value1.trim().equals(value2.trim())) {
					newHtml = newHtml.replace(tag, "");
				}else {
					newHtml = newHtml.replace(tag, content);
				}
				break;
			default:
				break;
			}
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}

}
