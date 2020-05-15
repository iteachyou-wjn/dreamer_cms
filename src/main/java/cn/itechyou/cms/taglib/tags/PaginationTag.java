package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.vo.ArchivesVo;

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
		
		CategoryWithBLOBs category = categoryService.queryCategoryByCode(typeid);
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
		String firstUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNavigateFirstPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		sb.append("<li><a href='"+firstUrl+"'>首页</a></li>");
		if(pageInfo.getPrePage() != 0) {
			String preUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPrePage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+preUrl+"'>上一页</a></li>");
		}
		
		for (int i = 0; i < pageInfo.getNavigatepageNums().length; i++) {
			int pageNum = pageInfo.getNavigatepageNums()[i];
			String pageUrl = pageurl.replace("{pageNum}", String.valueOf(pageNum)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			String className = "";
			if(pageInfo.getPageNum() == pageNum) {
				className = "active";
			}
			sb.append("<li class='"+className+"'><a href='"+pageUrl+"'>"+pageNum+"</a></li>");
		}
		
		if(pageInfo.getNextPage() != 0) {
			String nextUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNextPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+nextUrl+"'>下一页</a></li>");
		}
		String lastUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNavigateLastPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		sb.append("<li><a href='"+lastUrl+"'>尾页</a></li>");
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
		String keywords = params.getEntity().get("keywords").toString();
		String pageurl = "/search" + "?pageNum={pageNum}&pageSize={pageSize}&entity%5B%27keywords%27%5D=" + keywords; 
		
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
		String firstUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNavigateFirstPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		sb.append("<li><a href='"+firstUrl+"'>首页</a></li>");
		if(pageInfo.getPrePage() != 0) {
			String preUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getPrePage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+preUrl+"'>上一页</a></li>");
		}
		
		for (int i = 0; i < pageInfo.getNavigatepageNums().length; i++) {
			int pageNum = pageInfo.getNavigatepageNums()[i];
			String pageUrl = pageurl.replace("{pageNum}", String.valueOf(pageNum)).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			String className = "";
			if(pageInfo.getPageNum() == pageNum) {
				className = "active";
			}
			sb.append("<li class='"+className+"'><a href='"+pageUrl+"'>"+pageNum+"</a></li>");
		}
		
		if(pageInfo.getNextPage() != 0) {
			String nextUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNextPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
			sb.append("<li><a href='"+nextUrl+"'>下一页</a></li>");
		}
		String lastUrl = pageurl.replace("{pageNum}", String.valueOf(pageInfo.getNavigateLastPage())).replace("{pageSize}", String.valueOf(pageInfo.getPageSize()));
		sb.append("<li><a href='"+lastUrl+"'>尾页</a></li>");
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
