package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtils;

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
	public String parse(String html) {
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
				if(StringUtils.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			if(entity.containsKey("start") && entity.containsKey("length")) {
				params.setPageNum(Integer.parseInt(entity.get("start").toString()));
				params.setPageSize(Integer.parseInt(entity.get("length").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
			}

			List<CategoryWithBLOBs> list = null;
			
			CategoryWithBLOBs category = null;
			String code = StringUtils.isBlank(entity.get("type")) ? "top" : entity.get("type").toString();
			
			String isShow = null;
			if(!entity.containsKey("showall") || StringUtils.isBlank(entity.get("showall"))) {
				isShow = "1";
			}else {
				if(!"true".equalsIgnoreCase(entity.get("showall").toString())) {
					isShow = "1";
				}
			}
			if("top".equals(code)) {
				category = new CategoryWithBLOBs();
				category.setId("-1");
				list = categoryService.getTreeList(category.getId());
			}else {
				category = categoryService.queryCategoryByCode(entity.get("typeid").toString());
				if(category != null) {
					list = categoryService.getTreeList(category.getId(), isShow);
				}
			}
			
			StringBuilder sb = new StringBuilder();
			if(list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					CategoryWithBLOBs temp = list.get(j);
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
	public String parse(String html,String typeid) {
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
				if(StringUtils.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			if(entity.containsKey("start") && entity.containsKey("length")) {
				params.setPageNum(Integer.parseInt(entity.get("start").toString()));
				params.setPageSize(Integer.parseInt(entity.get("length").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
			}

			List<CategoryWithBLOBs> list = null;
			CategoryWithBLOBs category = null;
			String isShow = StringUtils.isBlank(entity.get("isall")) ? "1" : entity.get("isall").toString();
			
			category = categoryService.queryCategoryByCode(typeid);
			if(category != null) {
				list = categoryService.getTreeList(category.getId(), isShow);
			}
			
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < list.size(); j++) {
				CategoryWithBLOBs temp = list.get(j);
				String item = new String(content);
				
				item = this.buildHTML(item, temp, (j + 1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}
	
}
