package cc.iteachyou.cms.taglib.tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.iteachyou.cms.dao.CategoryMapper;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.ArchivesWithRownum;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.IParse;
import cc.iteachyou.cms.taglib.annotation.Attribute;
import cc.iteachyou.cms.taglib.annotation.Tag;
import cc.iteachyou.cms.taglib.utils.RegexUtil;
import cc.iteachyou.cms.taglib.utils.URLUtils;
import cc.iteachyou.cms.utils.StringUtil;

@Component
@Tag(beginTag="{dreamer-cms:prevnext /}",endTag="{/dreamer-cms:prevnext}",regexp="(\\{dreamer-cms:prevnext[ \\t]+.*/\\})|(\\{dreamer-cms:prevnext[ \\t]+.*\\}\\{/dreamer-cms:prevnext\\})", attributes={
	@Attribute(name = "layout",regex = "[ \t]+layout=[\"\'].*?[\"\']"),
})
public class PrevNextTag implements IParse {

	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private SystemService systemService;
	
	/**
	 * 执行类型：
	 * P：解析
	 * S：生成静态化
	 */
	private String t;
	
	@Override
	public String parse(String html) {
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public String parse(String html, String id) {
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Tag annotations = PrevNextTag.class.getAnnotation(Tag.class);
		Attribute[] attributes = annotations.attributes();
		List<String> all = RegexUtil.parseAll(html, annotations.regexp(), 0);
		if(StringUtil.isBlank(all)) {
			return html;
		}
		String newHtml = html;
		
		
		Archives archives = archivesService.selectByPrimaryKey(id);
		Category categoryWithBLOBs = categoryMapper.selectByPrimaryKey(archives.getCategoryId());
		//上一篇下一篇 
		Map<String,Object> params = new HashMap<String, Object>(); 
		params.put("arcid", id); 
		params.put("categoryId", archives.getCategoryId());
		ArchivesWithRownum currentArticle = archivesService.queryArticleRowNum(params);
		
		params.remove("arcid"); 
		params.put("privNum", (currentArticle.getRownum() - 1) + ""); 
		ArchivesWithRownum prevArc = archivesService.queryArticleRowNum(params);
		
		params.remove("privNum"); 
		params.put("nextNum", (currentArticle.getRownum() + 1) + ""); 
		ArchivesWithRownum nextArc = archivesService.queryArticleRowNum(params); 
		
		for (int i = 0; i < all.size(); i++) {
			String tag = all.get(i);
			Map<String,Object> entity = new HashMap<String,Object>();
			String prevNextTag = all.get(i);
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(prevNextTag, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			
			String layout = "";
			if(entity.containsKey("layout") && StringUtil.isNotBlank(entity.get("layout"))) {
				layout = entity.get("layout").toString();
			}
			
			StringBuilder prevnext = new StringBuilder();
			if("prev".equalsIgnoreCase(layout)) {
				prevnext.append("<ul class='dreamer-prevnext'>");
				if(prevArc != null) { 
					prevnext.append("<li class='dreamer-prev'>");
					prevnext.append("<span class='dreamer-prevnext-label'>上一篇：</span>");
					if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
						prevnext.append("<a href='/article/"+prevArc.getId() + "' title='" + prevArc.getTitle() + "'>" + prevArc.getTitle() + "</a>");
					}else {//生成
						String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
						Date createTime = prevArc.getCreateTime();
						String artDate = sdf.format(createTime);
						String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + prevArc.getId() + ".html";
						prevnext.append("<a href='" + artUrl + "' title='" + prevArc.getTitle() + "'>" + prevArc.getTitle() + "</a>");
					}
					prevnext.append("</li>");
				}else {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：没有了</span>");
				}
				prevnext.append("</ul>");
			}else if("next".equalsIgnoreCase(layout)) {
				prevnext.append("<ul class='dreamer-prevnext'>");
				if(nextArc != null) {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：</span>");
					if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
						prevnext.append("<a href='/article/"+nextArc.getId() + "' title='" + nextArc.getTitle() + "'>" + nextArc.getTitle() + "</a>");
					}else {//生成
						String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
						Date createTime = nextArc.getCreateTime();
						String artDate = sdf.format(createTime);
						String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + nextArc.getId() + ".html";
						prevnext.append("<a href='" + artUrl + "' title='" + nextArc.getTitle() + "'>" + nextArc.getTitle() + "</a>");
					}
					prevnext.append("</li>");
				}else {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：没有了</span>");
				}
				prevnext.append("</ul>");
			}else if("prev,next".equalsIgnoreCase(layout)) {
				prevnext.append("<ul class='dreamer-prevnext'>");
				if(prevArc != null) { 
					prevnext.append("<li class='dreamer-prev'>");
					prevnext.append("<span class='dreamer-prevnext-label'>上一篇：</span>");
					if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
						prevnext.append("<a href='/article/"+prevArc.getId() + "' title='" + prevArc.getTitle() + "'>" + prevArc.getTitle() + "</a>");
					}else {//生成
						String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
						Date createTime = prevArc.getCreateTime();
						String artDate = sdf.format(createTime);
						String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + prevArc.getId() + ".html";
						prevnext.append("<a href='" + artUrl + "' title='" + prevArc.getTitle() + "'>" + prevArc.getTitle() + "</a>");
					}
					prevnext.append("</li>");
				}else {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：没有了</span>");
				}
				
				if(nextArc != null) {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：</span>");
					if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
						prevnext.append("<a href='/article/"+nextArc.getId() + "' title='" + nextArc.getTitle() + "'>" + nextArc.getTitle() + "</a>");
					}else {//生成
						String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
						Date createTime = nextArc.getCreateTime();
						String artDate = sdf.format(createTime);
						String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + nextArc.getId() + ".html";
						prevnext.append("<a href='" + artUrl + "' title='" + nextArc.getTitle() + "'>" + nextArc.getTitle() + "</a>");
					}
					prevnext.append("</li>");
				}else {
					prevnext.append("<li class='dreamer-next'>");
					prevnext.append("<span class='dreamer-prevnext-label'>下一篇：没有了</span>");
				}
				
				prevnext.append("</ul>");
			}
			newHtml = newHtml.replace(tag, prevnext.toString());
		}
		return newHtml;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
}
