package cc.iteachyou.cms.taglib.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.LabelMapper;
import cc.iteachyou.cms.entity.Label;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.enums.FieldEnum;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * 标签列表标签
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:label}",endTag="{/dreamer-cms:label}",regexp="\\{dreamer-cms:label[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:label\\}", attributes={
	@Attribute(name = "start",regex = "[ \t]+start=[\"\'].*?[\"\']"),
	@Attribute(name = "length",regex = "[ \t]+length=[\"\'].*?[\"\']")
})
public class LabelTag implements IParse {

	@Autowired
	private LabelMapper labelMapper;
	
	@Override
	public String parse(String html) {
		Tag listAnnotation = LabelTag.class.getAnnotation(Tag.class);
		List<String> labelTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(labelTags == null || labelTags.size() <= 0) {
			return html;
		}
		
		Attribute[] attributes = listAnnotation.attributes();
		
		String newHtml = html;
		for (int i = 0;i < labelTags.size();i++) {
			String tag = labelTags.get(i);
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
			
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			List<Label> list = new ArrayList<>();
			if(entity.containsKey("start") && entity.containsKey("length")) {
				int start = Integer.parseInt(entity.get("start").toString());
				int length = Integer.parseInt(entity.get("length").toString());
				params.setPageNum(start <= 0 ? 1 : start);
				params.setPageSize(length <= 0 ? 10 : length);
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
				list = labelMapper.queryLabelByPage(params.getEntity());
			}else {
				list = labelMapper.selectAll();
			}
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < list.size(); j++) {
				Label label = list.get(j);
				String item = new String(content);
				item = item.replaceAll(FieldEnum.FIELD_AUTOINDEX.getRegexp(), String.valueOf(j + 1));
				item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), label.getId());
				item = item.replaceAll(FieldEnum.FIELD_TAGNAME.getRegexp(), label.getTagName());
				item = item.replaceAll(FieldEnum.FIELD_TAGPINYIN.getRegexp(), label.getPinyin());
				item = item.replaceAll(FieldEnum.FIELD_TAGFIRSTCHAR.getRegexp(), label.getFirstChar());
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		return null;
	}

}
