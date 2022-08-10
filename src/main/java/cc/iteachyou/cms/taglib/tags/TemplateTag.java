package cc.iteachyou.cms.taglib.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.Theme;
import cc.iteachyou.cms.service.ThemeService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Tag;

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
		String templatePath = "/" + Constant.UPLOAD_PREFIX + "templates/" + currentTheme.getThemePath() + "/";
		String newHtml = html.replaceAll(annotations.regexp(), templatePath);
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}

}
