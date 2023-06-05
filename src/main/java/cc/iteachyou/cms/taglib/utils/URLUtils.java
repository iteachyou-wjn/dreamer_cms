package cc.iteachyou.cms.taglib.utils;

import java.util.List;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.service.ArchivesService;
import cc.iteachyou.cms.utils.PinyinUtils;
import cc.iteachyou.cms.utils.StringUtil;
import cn.hutool.core.date.DateUtil;

public class URLUtils {
	
	/**
	 * 生成栏目URL
	 * @param system
	 * @param category
	 * @param t
	 * @return
	 */
	public static String parseURL(cc.iteachyou.cms.entity.System system, Category category, String t) {
		String staticdir = system.getStaticdir();
		if(staticdir.startsWith("/")) {
			staticdir = staticdir.substring(1);
		}
		String url = "";
		String typeCode = StringUtil.isBlank(category.getCode()) ? "" : category.getCode();
		String visitUrl = StringUtil.isBlank(category.getVisitUrl()) ? PinyinUtils.toPinyin(category.getCnname()) : category.getVisitUrl();
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		if(StringUtil.isBlank(t) || "P".equals(t)) {
			if(category.getCatModel() == 1) {
				url = "/cover-" + typeCode + visitUrl;
			}else if(category.getCatModel() == 2) {
				url = "/list-" + typeCode + visitUrl + "/1/"
						+ (StringUtil.isBlank(category.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : category.getPageSize().toString());
			}else if(category.getCatModel() == 3) {
				url = StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl();
			}
		}else {
			if(category.getCatModel() == 1) {
				url = "/" + staticdir + visitUrl + "/index.html";
			}else if(category.getCatModel() == 2) {
				url = "/" + staticdir + visitUrl + "/list-1.html";
			}else if(category.getCatModel() == 3) {
				url = StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl();
			}
		}
		return url;
	}
	
	public static String parseFileName(Category category, Integer pageNum) {
		return "/list-" + pageNum + ".html";
	}

	/**
	 * 获取栏目静态化目录
	 * @param category
	 * @return
	 */
	public static String getCategoryDir(Category category) {
		String visitUrl = StringUtil.isBlank(category.getVisitUrl()) ? PinyinUtils.toPinyin(category.getCnname()) : category.getVisitUrl();
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		return visitUrl;
	}
	
	/**
	 * 生成站点地图
	 * @param system
	 * @param lists
	 * @param xml
	 */
	public static void parseSiteMap(System system, ArchivesService archivesService, List<Category> lists, StringBuilder xml) {
		for (int i = 0; i < lists.size(); i++) {
			Category category = lists.get(i);
			String url = URLUtils.parseURL(system, category, system.getBrowseType() == 1 ? "P" : "S");
			xml.append("<url>");
			xml.append("<loc>" + system.getWebsite() + (url.startsWith("/") ? url.substring(1) : url) + "</loc>");
			xml.append("<priority>" + 0.5 + "</priority>");
			xml.append("<lastmod>" + DateUtil.format(category.getCreateTime(), "yyyy-MM-dd") + "</lastmod>");
			xml.append("<changefreq>monthly</changefreq>");
			xml.append("</url>");
			
			List<Archives> archives = archivesService.queryAll(category.getId());
			
			for (int j = 0; j < archives.size(); j++) {
				Archives archive = archives.get(j);
				String artUrl = "";
				if(system.getBrowseType() == 1) {//解析
					artUrl = "article/" + archive.getId();
				} else {//生成
					String categoryDir = URLUtils.getCategoryDir(category);
					String artDate = DateUtil.format(archive.getCreateTime(), "yyyy-MM-dd");
					artUrl = system.getStaticdir() + categoryDir + "/" + artDate + "/" + archive.getId() + ".html";
				}
				xml.append("<url>");
				xml.append("<loc>" + system.getWebsite() + artUrl + "</loc>");
				xml.append("<priority>" + 0.3 + "</priority>");
				xml.append("<lastmod>" + DateUtil.format(archive.getCreateTime(), "yyyy-MM-dd") + "</lastmod>");
				xml.append("<changefreq>weekly</changefreq>");
				xml.append("</url>");
			}
			
			List<Category> children = category.getNodes();
			if(children != null) {
				parseSiteMap(system, archivesService, children, xml);
			}
		}
	}
}
