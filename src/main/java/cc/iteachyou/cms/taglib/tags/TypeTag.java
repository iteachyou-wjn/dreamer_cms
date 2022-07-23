package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.ThemeService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * Type标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:type}",endTag="{/dreamer-cms:type}",regexp="\\{dreamer-cms:type[ \\t]+.*\\}([\\s\\S]+?)\\{/dreamer-cms:type\\}", attributes={
		@Attribute(name = "typeid",regex = "[ \t]+typeid=[\"\'].*?[\"\']"),
	})
public class TypeTag extends AbstractChannelTag implements IParse {
	
	@Autowired
	private ThemeService themeService;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public String parse(String html) throws CmsException {
		Tag annotations = TypeTag.class.getAnnotation(Tag.class);
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
			
			if(!entity.containsKey("typeid")) {
				return newHtml;
			}
			
			Category categoryWithBLOBs = categoryService.queryCategoryByCode(entity.get("typeid").toString());
			if(categoryWithBLOBs == null) {
				//throw new CmsException("aa","");
			}
			String item = new String(content);
			
			item = this.buildHTML(item, categoryWithBLOBs, 1);
			//type标签中，不涉及子节点
			//item = item.replaceAll(FieldEnum.FIELD_HASCHILDREN.getRegexp(), (categoryWithBLOBs.getNodes() == null || categoryWithBLOBs.getNodes().size() <= 0) ? "false" : "true");
			newHtml = newHtml.replace(tag, item);
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}
}
