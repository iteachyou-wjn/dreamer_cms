package cn.itechyou.blog.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.blog.entity.Variable;
import cn.itechyou.blog.service.SystemService;
import cn.itechyou.blog.service.VariableService;
import cn.itechyou.blog.taglib.IParse;
import cn.itechyou.blog.taglib.annotation.Attribute;
import cn.itechyou.blog.taglib.annotation.Tag;
import cn.itechyou.blog.taglib.utils.RegexUtil;
import cn.itechyou.blog.utils.StringUtil;

/**
 * Global标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:variable /}",endTag="",regexp="(\\{dreamer-cms:variable[ \\t]+.*?/\\})|(\\{dreamer-cms:variable[ \\t]+.*\\}\\{/dreamer-cms:variable\\})", attributes={
		@Attribute(name = "name",regex = "[ \t]+name=[\"\'].*?[\"\']"),
	})
public class VariableTag implements IParse {

	@Autowired
	private VariableService variableService;
	
	@Override
	public String parse(String html) {
		Tag annotations = VariableTag.class.getAnnotation(Tag.class);
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(all)) {
			return html;
		}
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
			
			if(entity.containsKey("name")) {
				String name = entity.get("name").toString();
				Variable variable = variableService.queryVariableByName(name);
				newHtml = newHtml.replace(string, variable.getValue());
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
