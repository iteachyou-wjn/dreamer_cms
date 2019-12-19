package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.ArchivesMapper;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.StringUtil;

/**
 * List标签解析器
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:list}",endTag="{/dreamer-cms:list}",regexp="\\{dreamer-cms:list[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:list\\}", attributes={
	@Attribute(name = "typeid",regex = "[ \t]+typeid=[\"\'].*?[\"\']"),
	@Attribute(name = "start",regex = "[ \t]+start=[\"\'].*?[\"\']"),
	@Attribute(name = "length",regex = "[ \t]+length=[\"\'].*?[\"\']"),
	@Attribute(name = "flag",regex = "[ \t]+flag=[\"\'].*?[\"\']"),
	@Attribute(name = "addfields",regex = "[ \t]+addfields=[\"\'].*?[\"\']"),
	@Attribute(name = "formkey",regex = "[ \t]+formkey=[\"\'].*?[\"\']"),
	@Attribute(name = "sortWay",regex = "[ \t]+sortWay=[\"\'].*?[\"\']"),
	@Attribute(name = "sortBy",regex = "[ \t]+sortBy=[\"\'].*?[\"\']")
})
public class ListTag extends AbstractListTag implements IParse {

	@Autowired
	private SystemService systemService;
	@Autowired
	private ArchivesMapper archivesMapper;
	@Autowired
	private FormService formService;
	
	@Override
	public String parse(String html) {
		Tag listAnnotation = ListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		cn.itechyou.cms.entity.System system = systemService.getSystem();
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
			entity.put("cid", entity.containsKey("typeid") ? entity.get("typeid").toString() : "");
			entity.put("sortWay", entity.containsKey("sortWay") ? entity.get("sortWay") : "asc");
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			if(entity.containsKey("start") && entity.containsKey("length")) {
				params.setPageNum(Integer.parseInt(entity.get("start").toString()));
				params.setPageSize(Integer.parseInt(entity.get("length").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
			}
			List<Map<String, Object>> list = archivesMapper.queryListByPage(entity);
			
			String[] addfields = new String[] {};
			//获取附加字段
			if(entity.containsKey("addfields")) {
				String addfieldsStr = entity.get("addfields").toString();
				addfields = addfieldsStr.split(",");
			}
			
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> archivesVo = list.get(j);
				String imagePath = "";
				if(archivesVo.containsKey("imagePath")) {
					imagePath = archivesVo.get("imagePath").toString();
					imagePath = imagePath.replace("\\", "/");
				}
				String item = new String(content);
				item = this.buildHTML(item, archivesVo, addfields, (j + 1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}
	
	@Override
	public String parse(String html,String typeid) {
		Tag listAnnotation = ListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		cn.itechyou.cms.entity.System system = systemService.getSystem();
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
			if(entity.containsKey("length")) {
				params.setPageNum(Integer.parseInt(entity.containsKey("start") ? entity.get("start").toString() : "0"));
				params.setPageSize(Integer.parseInt(entity.get("length").toString()));
				PageHelper.startPage(params.getPageNum(), params.getPageSize());
			}
			List<Map<String, Object>> list = archivesMapper.queryListByPage(entity);
			
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
				String imagePath = "";
				if(archivesVo.containsKey("imagePath")) {
					imagePath = archivesVo.get("imagePath").toString();
					imagePath = imagePath.replace("\\", "/");
				}
				item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), archivesVo.get("aid").toString());
				item = item.replaceAll(FieldEnum.FIELD_TITLE.getRegexp(), archivesVo.get("title").toString());
				item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), "url");
				item = item.replaceAll(FieldEnum.FIELD_LITPIC.getRegexp(), system.getWebsite() + system.getUploaddir() + "/" + imagePath);
				
				for (int k = 0; k < addfields.length; k++) {
					String field = addfields[k];
					item = item.replaceAll(FieldEnum.FIELD_ADDFIELDS_START.getRegexp() + field + FieldEnum.FIELD_ADDFIELDS_END.getRegexp(), archivesVo.containsKey(field) ? archivesVo.get(field).toString() : "");
				}
				
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
		}
		return newHtml;
	}

}