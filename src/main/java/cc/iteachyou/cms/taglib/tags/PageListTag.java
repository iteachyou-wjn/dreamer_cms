package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ArchivesMapper;
import cc.iteachyou.cms.dao.CategoryMapper;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.vo.ArchivesVo;

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
	public String parse(String html, String typeid, Integer pageNum, Integer pageSize) throws CmsException {
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
				Category categoryWithBLOBs = categoryMapper.queryCategoryByCode(typeid);
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
			if(list == null || list.size() <= 0) {
				newHtml = newHtml.replace(tag, "<div class='dreamer-empty'>暂无数据</div>");
			}else {
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> archivesVo = list.get(j);
					String item = new String(content);
					item = this.buildHTML(item, archivesVo, addfields, (j+1));
					sb.append(item);
				}
				newHtml = newHtml.replace(tag, sb.toString());
			}
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
	public String parse(String html, SearchEntity params) throws CmsException {
		Tag listAnnotation = PageListTag.class.getAnnotation(Tag.class);
		List<String> listTags = RegexUtil.parseAll(html, listAnnotation.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, listAnnotation.regexp(), 1);
		
		if(listTags == null || listTags.size() <= 0) {
			return html;
		}
		
		Map<String,Object> searchParams = new HashMap<String,Object>();
		//关键词
		if(params.getEntity().containsKey("keywords") && StringUtil.isNotBlank(params.getEntity().get("keywords"))) {
			String keywords = params.getEntity().get("keywords").toString();
			searchParams.put("keywords", keywords);
		}
		//栏目,可指定多个，多个用英文逗号隔开
		if(params.getEntity().containsKey("typeid") && StringUtil.isNotBlank(params.getEntity().get("typeid"))) {
			String typeid = params.getEntity().get("typeid").toString();
			typeid = typeid.replace(",", "','");
			searchParams.put("typeid", "'" + typeid + "'");
		}
		if(params.getEntity().containsKey("tag") && StringUtil.isNotBlank(params.getEntity().get("tag"))) {
			String tag = params.getEntity().get("tag").toString();
			searchParams.put("tag", tag);
		}
		
		String newHtml = html;
		for (int i = 0;i < listTags.size();i++) {
			String tag = listTags.get(i);
			String content = contents.get(i);
			Map<String,Object> entity = params.getEntity();
			entity.put("sortWay", entity.containsKey("sortWay") ? entity.get("sortWay") : "asc");
			params.setEntity(entity);
			//开始分页
			PageHelper.startPage(params.getPageNum(), params.getPageSize());
			List<ArchivesVo> list = archivesMapper.queryListByKeywords(searchParams);
			PageInfo<ArchivesVo> pageInfo = new PageInfo<ArchivesVo>(list);
			
			StringBuilder sb = new StringBuilder();
			if(list == null || list.size() <= 0) {
				newHtml = newHtml.replace(tag, "<div class='dreamer-empty'>暂无数据</div>");
			}else {
				for (int j = 0; j < list.size(); j++) {
					ArchivesVo archivesVo = list.get(j);
					String item = new String(content);
					item = this.buildHTML(item, archivesVo, (j+1));
					sb.append(item);
				}
			}
			newHtml = newHtml.replace(tag, sb.toString());
			newHtml = paginationTag.parse(newHtml, params, pageInfo);
		}
		return newHtml;
	}
}