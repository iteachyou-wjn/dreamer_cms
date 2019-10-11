package cn.itechyou.cms.taglib.tags;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.StringUtil;

/**
 * Include标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:include /}",endTag="{/dreamer-cms:include}",regexp="(\\{dreamer-cms:include[ \\t]+.*/\\})|(\\{dreamer-cms:include[ \\t]+.*\\}\\{/dreamer-cms:include\\})", attributes={
		@Attribute(name = "file",regex = "[ \t]+file=[\"\'].*?[\"\']"),
	})
public class IncludeTag implements IParse {
	
	@Autowired
	private ThemeService themeService;
	@Autowired
	private FileConfiguration fileConfiguration;

	@Override
	public String parse(String html) {
		Tag annotations = IncludeTag.class.getAnnotation(Tag.class);
		Attribute[] attributes = annotations.attributes();
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(all)) {
			return html;
		}
		String newHtml = html;
		
		String resourceDir = fileConfiguration.getResourceDir() + "templates/";
		Theme currentTheme = themeService.getCurrentTheme();
		String templatePath = currentTheme.getThemePath() + "/";
		String basePath = resourceDir + templatePath;
		for (int i = 0; i < all.size(); i++) {
			Map<String,Object> entity = new HashMap<String,Object>();
			String includeTag = all.get(i);
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(includeTag, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			
			if(entity.keySet() != null && entity.keySet().size() > 0) {
				String path = basePath + entity.get("file").toString();
				File includeFile = new File(path);
				String includeHtml;
				try {
					includeHtml = FileUtils.readFileToString(includeFile, "UTF-8");
					newHtml = newHtml.replaceFirst(annotations.regexp(), includeHtml);
				} catch (IOException e) {
					e.printStackTrace();
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
