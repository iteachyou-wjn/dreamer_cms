package cc.iteachyou.cms.taglib.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ArchivesMapper;
import cc.iteachyou.cms.dao.CategoryMapper;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * List标签解析器
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:list}",endTag="{/dreamer-cms:list}",regexp="\\{dreamer-cms:list[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:list\\}", attributes={
	@Attribute(name = "typeid",regex = "[ \t]+typeid=[\"\'].*?[\"\']"),
	@Attribute(name = "pagenum",regex = "[ \t]+pagenum=[\"\'].*?[\"\']"),
	@Attribute(name = "pagesize",regex = "[ \t]+pagesize=[\"\'].*?[\"\']"),
	@Attribute(name = "flag",regex = "[ \t]+flag=[\"\'].*?[\"\']"),
	@Attribute(name = "addfields",regex = "[ \t]+addfields=[\"\'].*?[\"\']"),
	@Attribute(name = "formkey",regex = "[ \t]+formkey=[\"\'].*?[\"\']"),
	@Attribute(name = "cascade",regex = "[ \t]+cascade=[\"\'].*?[\"\']"),
	@Attribute(name = "sortWay",regex = "[ \t]+sortWay=[\"\'].*?[\"\']"),
	@Attribute(name = "sortBy",regex = "[ \t]+sortBy=[\"\'].*?[\"\']"),
})
public class ListTag extends AbstractListTag implements IParse {

	@Autowired
	private SystemService systemService;
	@Autowired
	private ArchivesMapper archivesMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private FormService formService;
	
	@Override
	public String parse(String html) throws CmsException {
		Tag listAnnotation = ListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		Attribute[] attributes = listAnnotation.attributes();
		
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
				if("formkey".equals(key)) {
					Form form = formService.queryFormByCode(value);
					String tableName = "system_" + form.getTableName();
					entity.put("tableName", tableName);
				}else {
					entity.put(key, value);
				}
			}
			String typeid = entity.containsKey("typeid") ? entity.get("typeid").toString() : "";
			entity.put("cid", typeid);
			entity.put("sortWay", entity.containsKey("sortWay") ? entity.get("sortWay") : "asc");
			String cascade = entity.containsKey("cascade") ? entity.get("cascade").toString() : "false";
			if("true".equals(cascade)) {
				Category categoryWithBLOBs = categoryMapper.queryCategoryByCode(typeid);
				String catSeq = categoryWithBLOBs.getCatSeq();
				entity.put("cascade", catSeq);
			}
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);

			//查询结果集
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if(entity.containsKey("pagenum") && entity.containsKey("pagesize")) {
				params.setPageNum(Integer.parseInt(entity.get("pagenum").toString()));
				params.setPageSize(Integer.parseInt(entity.get("pagesize").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
				list = archivesMapper.queryListByPage(entity);
			}else {
				list = archivesMapper.queryListByPage(entity);
			}
			
			String[] addfields = new String[] {};
			//获取附加字段
			if(entity.containsKey("addfields")) {
				String addfieldsStr = entity.get("addfields").toString();
				addfields = addfieldsStr.split(",");
			}
			
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> archivesVo = list.get(j);
				String item = new String(content);
				item = this.buildHTML(item, archivesVo, addfields, (j + 1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}
	
	@Override
	public String parse(String html,String typeid) throws CmsException {
		Tag listAnnotation = ListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		Attribute[] attributes = listAnnotation.attributes();
		
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
				if("formkey".equals(key)) {
					Form form = formService.queryFormByCode(value);
					String tableName = "system_" + form.getTableName();
					entity.put("tableName", tableName);
				}else {
					entity.put(key, value);
				}
			}
			entity.put("sortWay", entity.containsKey("sortWay") ? entity.get("sortWay") : "asc");
			entity.put("cid", typeid);
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			
			//查询结果集
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if(entity.containsKey("pagenum") && entity.containsKey("pagesize")) {
				params.setPageNum(Integer.parseInt(entity.get("pagenum").toString()));
				params.setPageSize(Integer.parseInt(entity.get("pagesize").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
				list = archivesMapper.queryListByPage(entity);
			}else {
				list = archivesMapper.queryListByPage(entity);
			}
			
			StringBuilder sb = new StringBuilder();
			
			String[] addfields = new String[] {};
			//获取附加字段
			if(entity.containsKey("addfields")) {
				String addfieldsStr = entity.get("addfields").toString();
				addfields = addfieldsStr.split(",");
			}
			
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> archivesVo = list.get(j);
				String item = new String(content);
				item = this.buildHTML(item, archivesVo, addfields, (j + 1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

}