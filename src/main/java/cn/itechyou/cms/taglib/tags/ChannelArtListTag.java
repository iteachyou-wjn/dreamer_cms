package cn.itechyou.cms.taglib.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.entity.CategoryWithBLOBs;
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
})
public class ChannelArtListTag extends AbstractChannelTag implements IParse {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ListTag listTag;
	@Autowired ChannelTag channelTag;
	
	@Override
	public String parse(String html) {
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
			
			CategoryWithBLOBs category = null;
			List<CategoryWithBLOBs> categorys = new ArrayList<CategoryWithBLOBs>();
			
			if(!entity.containsKey("typeid")) {
				category = new CategoryWithBLOBs();
				category.setId("-1");
				categorys = categoryService.getTreeList(category.getId());
			}else {
				String code = entity.get("typeid").toString();
				entity.remove("typeid");
				
				category = categoryService.queryCategoryByCode(code);
				if(category == null) {
					//
				}
				categorys = categoryService.getTreeList(category.getId());
			}
			StringBuilder sb = new StringBuilder();
			if(categorys == null || categorys.size() <= 0) {
				String item = new String(content);
				item = listTag.parse(item, category.getCode());
				item = channelTag.parse(item, category.getCode());
				item = this.buildHTML(item, category, 1);
				sb.append(item);
			}else {
				for (int j = 0; j < categorys.size(); j++) {
					CategoryWithBLOBs categoryWithBLOBs = categorys.get(j);
					String item = new String(content);
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