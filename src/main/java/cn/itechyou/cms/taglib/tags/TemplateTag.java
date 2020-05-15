package cn.itechyou.cms.taglib.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.entity.Theme;
import cn.itechyou.cms.service.ThemeService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.utils.FileConfiguration;

/**
 * Template标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:template /}", endTag="", regexp="(\\{dreamer-cms:template[ \\t]+/\\})|(\\{dreamer-cms:template[ \\t]+\\}\\{/dreamer-cms:template\\})",attributes={})
public class TemplateTag implements IParse {

	@Autowired
	private ThemeService themeService;
	
	@Override
	public String parse(String html) {
		Tag annotations = TemplateTag.class.getAnnotation(Tag.class);
		Theme currentTheme = themeService.getCurrentTheme();
		String templatePath = "/templates/" + currentTheme.getThemePath() + "/";
		String newHtml = html.replaceAll(annotations.regexp(), templatePath);
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}

}
