package cc.iteachyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.utils.PinyinUtils;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.vo.ArchivesVo;

@Component
@Tag(beginTag="{dreamer-cms:pagination /}",endTag="",regexp="(\\{dreamer-cms:pagination[ \\t]+.*?/\\})|(\\{dreamer-cms:pagination[ \\t]+.*\\}\\{/dreamer-cms:pagination\\})", attributes={
		@Attribute(name = "name",regex = "[ \t]+name=[\"\'].*?[\"\']"),
	})
public class PaginationTag implements IParse {
	
	/**
	 * 执行类型：
	 * P：解析
	 * S：生成静态化
	 */
	private String t;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public String parse(String html) {
		return null;
	}

	@Override
	public String parse(String html, String params) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 列表页面分页
	 * @param html
	 * @param typeid
	 * @param pageInfo
	 * @return
	 */
	public String parse(String html, String typeid, PageInfo<Map<String, Object>> pageInfo) {
		Tag annotations = PaginationTag.class.getAnnotation(Tag.class);
		String tag = RegexUtil.parseFirst(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(tag)) {
			return html;
		}
		String newHtml = html;
		
		Category category = categoryService.queryCategoryByCode(typeid);
		String typeCode = StringUtil.isBlank(category.getCode()) ? "" : category.getCode();
		String visitUrl = StringUtil.isBlank(category.getVisitUrl()) ? PinyinUtils.toPinyin(category.getCnname()) : category.getVisitUrl();
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		
		String pageurl;
		if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
			pageurl = "/list-" + typeCode + visitUrl + "/{pageNum}/{pageSize}"; 
		}else {
			pageurl = visitUrl + "/list-{pageNum}.html";
		}
		
		
		Attribute[] attributes = annotations.attributes();
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
		
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='dreamer-pagination'>");
		sb.append("<ul class='dreamer-ul'>");
		//
		String firstUrl = pageurl.replace("{pageNum}", String.valueOf(1)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		if(pageInfo.getTotal() > 0) {
			sb.append("<li><a href='"+firstUrl+"' title='首页'><i class='fa fa-angle-double-left'></i></a></li>");
		}
		if(pageInfo.isHasPreviousPage()) {
			String preUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPrePage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+preUrl+"' title='上一页'><i class='fa fa-angle-left'></i></a></li>");
		}
		
		for (int i = 0; i < pageInfo.getNavigatepageNums().length; i++) {
			int pageNum = pageInfo.getNavigatepageNums()[i];
			String pageUrl = pageurl.replace("{pageNum}", String.valueOf(pageNum)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			String className = "";
			if(pageInfo.getPageNum() == pageNum) {
				className = "active";
			}
			sb.append("<li class='"+className+"'><a href='"+pageUrl+"' title='第" + pageNum + "页'>"+pageNum+"</a></li>");
		}
		
		if(pageInfo.isHasNextPage()) {
			String nextUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNextPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+nextUrl+"' title='下一页'><i class='fa fa-angle-right'></i></a></li>");
		}
		String lastUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPages())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		if(pageInfo.getTotal() > 0) {
			sb.append("<li><a href='"+lastUrl+"' title='尾页'><i class='fa fa-angle-double-right'></i></a></li>");
		}
		sb.append("</ul>");
		sb.append("</div>");
		
		newHtml = newHtml.replace(tag, sb.toString());
		return newHtml;
	}

	/**
	 * 搜索列表页面分页
	 * @param html
	 * @param pageInfo
	 * @return
	 */
	public String parse(String html, SearchEntity params, PageInfo<ArchivesVo> pageInfo) {
		Tag annotations = PaginationTag.class.getAnnotation(Tag.class);
		String tag = RegexUtil.parseFirst(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(tag)) {
			return html;
		}
		String newHtml = html;
		
		/**
		 * 转义字符
		 * %5B:[
		 * %27:'
		 * %5D:]
		 */
		String pageurl = "/search" + "?pageNum={pageNum}&pageSize={pageSize}";
		if(params.getEntity().containsKey("keywords") && StringUtil.isNotBlank(params.getEntity().get("keywords"))) {
			String keywords = params.getEntity().get("keywords").toString();
			pageurl += "&entity%5B%27keywords%27%5D=" + keywords;
		}
		if(params.getEntity().containsKey("tag") && StringUtil.isNotBlank(params.getEntity().get("tag"))) {
			String label = params.getEntity().get("tag").toString();
			pageurl += "&entity%5B%27tag%27%5D=" + label;
		}
		
		Attribute[] attributes = annotations.attributes();
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
		
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='dreamer-pagination'>");
		sb.append("<ul class='dreamer-ul'>");
		//
		if(pageInfo.getTotal() > 0) {
			String firstUrl = pageurl.replace("{pageNum}", String.valueOf(1)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+firstUrl+"' title='首页'><i class='fa fa-angle-double-left'></i></a></li>");
		}
		if(pageInfo.isHasPreviousPage()) {
			String preUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPrePage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+preUrl+"' title='上一页'><i class='fa fa-angle-left'></i></a></li>");
		}
		
		for (int i = 0; i < pageInfo.getNavigatepageNums().length; i++) {
			int pageNum = pageInfo.getNavigatepageNums()[i];
			String pageUrl = pageurl.replace("{pageNum}", String.valueOf(pageNum)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			String className = "";
			if(pageInfo.getPageNum() == pageNum) {
				className = "active";
			}
			sb.append("<li class='"+className+"'><a href='"+pageUrl+"' title='第" + pageNum + "页'>"+pageNum+"</a></li>");
		}
		
		if(pageInfo.isHasNextPage()) {
			String nextUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNextPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+nextUrl+"' title='下一页'><i class='fa fa-angle-right'></i></a></li>");
		}
		if(pageInfo.getTotal() > 0) {
			String lastUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPages())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+lastUrl+"' title='尾页'><i class='fa fa-angle-double-right'></i></a></li>");
		}
		sb.append("</ul>");
		sb.append("</div>");
		
		newHtml = newHtml.replace(tag, sb.toString());
		return newHtml;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
}
