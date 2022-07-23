package cc.iteachyou.cms.taglib.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.dao.CategoryMapper;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.taglib.utils.URLUtils;
import cc.iteachyou.cms.utils.StringUtil;

@Component
@Tag(beginTag="{dreamer-cms:location}",endTag="{/dreamer-cms:location}",regexp="\\{dreamer-cms:location[\\s]*.*?/\\}", attributes={
	@Attribute(name = "lang",regex = "[ \t]+lang=[\"\'].*?[\"\']"),
})
public class LocationTag implements IParse {
	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryMapper categoryMapper;
	
	/**
	 * 执行类型：
	 * P：解析
	 * S：生成静态化
	 */
	private String t;
	
	@Override
	public String parse(String html) throws CmsException {
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		
		Tag locationAnnotation = LocationTag.class.getAnnotation(Tag.class);
		List<String> locationTags = RegexUtil.parseAll(html, locationAnnotation.regexp(), 0);
		
		if(locationTags == null || locationTags.size() <= 0) {
			return html;
		}
		
		System.out.println(locationTags);
		return html;
	}

	@Override
	public String parse(String html, String params) throws CmsException {
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		
		Tag locationAnnotation = LocationTag.class.getAnnotation(Tag.class);
		List<String> locationTags = RegexUtil.parseAll(html, locationAnnotation.regexp(), 0);
		Attribute[] attributes = locationAnnotation.attributes();
		if(locationTags == null || locationTags.size() <= 0) {
			return html;
		}
		
		String newHtml = new String(html);
			
		Category category = categoryMapper.queryCategoryByCode(params);
		
		String catSeq = category.getCatSeq();
		catSeq = catSeq.substring(1);
		String[] categoryCodes = catSeq.split("\\.");
		
		List<Category> categorys = new ArrayList<>();
		for(int i = 0;i < categoryCodes.length;i++) {
			Category cate = categoryMapper.queryCategoryByCode(categoryCodes[i]);
			categorys.add(cate);
		}
		
		for(int i = 0;i < locationTags.size();i++) {
			String tag = locationTags.get(i);
			Map<String,Object> entity = new HashMap<String,Object>();
			
			StringBuilder location = new StringBuilder();
			location.append("<ul class='dreamer-location'>");
			location.append("<li>");
			location.append("<a href='/' title='首页'>").append("首页").append("</a>");
			location.append("</li>");
			
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
				
				for(int j = 0;j < categorys.size();j++) {
					Category cate = categorys.get(j);
					String typeUrl = URLUtils.parseURL(system, cate, this.t);
					location.append("<li>");
					if(entity.containsKey("lang") && "en".equalsIgnoreCase(entity.get("lang").toString())) {
						location.append("<a href='" + typeUrl + "' title='" + cate.getEnname() + "'>").append(cate.getEnname()).append("</a>");
					}else {
						location.append("<a href='" + typeUrl + "' title='" + cate.getCnname() + "'>").append(cate.getCnname()).append("</a>");
					}
					location.append("</li>");
				}
				
			}
			
			System.out.println(entity);
			
			location.append("</ul>");
			newHtml = newHtml.replace(tag, location.toString());
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
