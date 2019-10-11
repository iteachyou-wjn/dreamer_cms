package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.StringUtil;

/**
 * Global标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:global /}",endTag="",regexp="(\\{dreamer-cms:global[ \\t]+.*?/\\})|(\\{dreamer-cms:global[ \\t]+.*\\}\\{/dreamer-cms:global\\})", attributes={
		@Attribute(name = "name",regex = "[ \t]+name=[\"\'].*?[\"\']"),
	})
public class GlobalTag implements IParse {

	@Autowired
	private SystemService systemService;
	
	@Override
	public String parse(String html) {
		Tag annotations = GlobalTag.class.getAnnotation(Tag.class);
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
			
			if(entity.containsKey("name")) {
				String name = entity.get("name").toString();
				if("website".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getWebsite());
				}else if ("title".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getTitle());
				}else if ("keywords".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getKeywords());
				}else if ("describe".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getDescribe());
				}else if ("icp".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getIcp());
				}else if ("copyright".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getCopyright());
				}else if ("uploaddir".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getUploaddir());
				}else if ("appid".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getAppid());
				}else if ("appkey".equalsIgnoreCase(name)) {
					newHtml = newHtml.replace(string, system.getAppkey());
				}
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
