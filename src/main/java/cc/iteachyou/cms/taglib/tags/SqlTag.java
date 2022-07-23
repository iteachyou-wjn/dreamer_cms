package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.dao.SqlMapper;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.XssAndSqlException;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * Sql标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:sql}",endTag="{/dreamer-cms:sql}",regexp="\\{dreamer-cms:sql[ \\t]+.*\\}([\\s\\S]+?)\\{/dreamer-cms:sql\\}", attributes={
		@Attribute(name = "sql",regex = "[ \t]+sql=[\"\'].*?[\"\']"),
	})
public class SqlTag extends AbstractChannelTag implements IParse {
	
	@Autowired
	private SqlMapper sqlMapper;

	@Override
	public String parse(String html) throws CmsException {
		Tag annotations = SqlTag.class.getAnnotation(Tag.class);
		Attribute[] attributes = annotations.attributes();
		List<String> tags = RegexUtil.parseAll(html, annotations.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, annotations.regexp(), 1);
		if(StringUtil.isBlank(tags)) {
			return html;
		}
		String newHtml = html;
		
		for (int i = 0; i < tags.size(); i++) {
			String sql = "";
			String tag = tags.get(i);
			String content = contents.get(i);
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(tag, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				sql = RegexUtil.parseFirst(condition, "sql=[\\\"\\'](.*)?[\\\"\\']", 1);
			}
			
			if(StringUtil.isBlank(sql)) {
				return newHtml;
			}
			
			sql = sql.replaceAll("\\[", "'").replaceAll("\\]", "'");
			
			if(!sql.startsWith("select") && !sql.startsWith("SELECT")) {
				throw new XssAndSqlException(
						ExceptionEnum.XSS_SQL_EXCEPTION.getCode(), 
						ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(), 
						"为了保证系统安全，Sql标签目前只支持查询操作。");
			}
			
			List<Map<String, Object>> list = sqlMapper.execute(sql);
			if(list == null) {
				return newHtml;
			}
			
			String result = "";
			for(int idx = 0;idx < list.size();idx++) {
				String item = new String(content);
				Map<String, Object> map = list.get(idx);
				
				int count = RegexUtil.count(item, "\\[field:(.*?)/\\]");
				
				for(int f = 0;f < count;f++) {
					String fieldName = RegexUtil.parseFirst(item, "\\[field:(.*?)/\\]", 1);
					if(map.containsKey(fieldName) && StringUtil.isNotBlank(map.get(fieldName))) {
						String value = map.get(fieldName).toString();
						if("litpic".equals(fieldName)) {//针对图片路径做特殊处理
							value = value.replace("\\","/");
						}else {//其它字段，则\替换为\\
							value = value.replace("\\","\\\\");
						}
						item = item.replaceFirst("\\[field:(.*?)/\\]", value);
					}
				}
				
				result += item;
			}
			newHtml = newHtml.replace(tag, result);
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}
}
