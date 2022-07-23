package cc.iteachyou.cms.taglib.utils;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.utils.PinyinUtils;
import cc.iteachyou.cms.utils.StringUtil;

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
}
