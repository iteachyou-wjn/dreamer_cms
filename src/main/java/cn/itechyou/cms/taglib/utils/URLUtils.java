package cn.itechyou.cms.taglib.utils;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtil;

public class URLUtils {
	/**
	 * 生成栏目URL
	 * @param system
	 * @param category
	 * @param t
	 * @return
	 */
	public static String parseURL(cn.itechyou.cms.entity.System system, CategoryWithBLOBs category, String t) {
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
				url = "/" + system.getStaticdir() + visitUrl + "/index.html";
			}else if(category.getCatModel() == 2) {
				url = "/" + system.getStaticdir() + visitUrl + "/list-1.html";
			}else if(category.getCatModel() == 3) {
				url = StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl();
			}
		}
		return url;
	}
	
	public static String parseFileName(CategoryWithBLOBs category, Integer pageNum) {
		return "/list-" + pageNum + ".html";
	}

	/**
	 * 获取栏目静态化目录
	 * @param category
	 * @return
	 */
	public static String getCategoryDir(CategoryWithBLOBs category) {
		String visitUrl = StringUtil.isBlank(category.getVisitUrl()) ? PinyinUtils.toPinyin(category.getCnname()) : category.getVisitUrl();
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		return visitUrl;
	}
}
