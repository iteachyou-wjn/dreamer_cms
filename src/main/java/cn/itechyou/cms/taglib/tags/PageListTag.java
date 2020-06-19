package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.ArchivesMapper;
import cn.itechyou.cms.dao.CategoryMapper;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.vo.ArchivesVo;

/**
 * List标签解析器
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:pagelist}",endTag="{/dreamer-cms:pagelist}",regexp="\\{dreamer-cms:pagelist[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:pagelist\\}", attributes={
	@Attribute(name = "formkey",regex = "[ \t]+formkey=[\"\'].*?[\"\']"),
	@Attribute(name = "addfields",regex = "[ \t]+addfields=[\"\'].*?[\"\']"),
	@Attribute(name = "cascade",regex = "[ \t]+cascade=[\"\'].*?[\"\']"),
	@Attribute(name = "sortWay",regex = "[ \t]+sortWay=[\"\'].*?[\"\']"),
	@Attribute(name = "sortBy",regex = "[ \t]+sortBy=[\"\'].*?[\"\']")
})
public class PageListTag extends AbstractListTag implements IParse {

	@Autowired
	private ArchivesMapper archivesMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private FormService formService;
	@Autowired
	private PaginationTag paginationTag;
	
	@Override
	public String parse(String html) {
		return null;
	}
	
	@Override
	public String parse(String html,String typeid) {
		return null;
	}

	/**
	 * 列表页面解析
	 * @param html
	 * @param typeid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public String parse(String html, String typeid, Integer pageNum, Integer pageSize) {
		Tag listAnnotation = PageListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
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
			String cascade = entity.containsKey("cascade") ? entity.get("cascade").toString() : "false";
			if("true".equals(cascade)) {
				CategoryWithBLOBs categoryWithBLOBs = categoryMapper.queryCategoryByCode(typeid);
				String catSeq = categoryWithBLOBs.getCatSeq();
				entity.put("cascade", catSeq);
			}
			SearchEntity params = new SearchEntity();
			params.setEntity(entity);
			//开始分页
			PageHelper.startPage(pageNum, pageSize);
			List<Map<String, Object>> list = archivesMapper.queryListByPage(entity);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
			
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
				item = this.buildHTML(item, archivesVo, addfields, (j+1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
			paginationTag.setT(this.getT());
			newHtml = paginationTag.parse(newHtml, typeid, pageInfo);
		}
		return newHtml;
	}

	/**
	 * 搜索列表页面解析
	 * @param html
	 * @param params
	 * @return
	 */
	public String parse(String html, SearchEntity params) {
		Tag listAnnotation = PageListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		//关键词
		String keywords = params.getEntity().get("keywords").toString();
		
		String newHtml = html;
		for (int i = 0;i < listTags.size();i++) {
			String tag = listTags.get(i);
			String content = contents.get(i);
			Map<String,Object> entity = params.getEntity();
			entity.put("sortWay", entity.containsKey("sortWay") ? entity.get("sortWay") : "asc");
			params.setEntity(entity);
			//开始分页
			PageHelper.startPage(params.getPageNum(), params.getPageSize());
			List<ArchivesVo> list = archivesMapper.queryListByKeywords(keywords);
			PageInfo<ArchivesVo> pageInfo = new PageInfo<ArchivesVo>(list);
			
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < list.size(); j++) {
				ArchivesVo archivesVo = list.get(j);
				String item = new String(content);
				item = this.buildHTML(item, archivesVo, (j+1));
				sb.append(item);
			}
			newHtml = newHtml.replace(tag, sb.toString());
			newHtml = paginationTag.parse(newHtml, params, pageInfo);
		}
		return newHtml;
	}
}