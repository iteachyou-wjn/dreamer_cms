package cn.itechyou.cms.taglib.tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.URLUtils;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.vo.ArchivesVo;

/**
 * 列表标签抽象类
 * @author Wangjn
 *
 */
public abstract class AbstractListTag {
	
	/**
	 * 执行类型：
	 * P：解析
	 * S：生成静态化
	 */
	private String t;
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private CategoryService categoryService;
	
	public String buildHTML(String item, Map<String, Object> archives, String[] addfields, int i) {
		cn.itechyou.cms.entity.System system = systemService.getSystem();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		CategoryWithBLOBs categoryWithBLOBs = null;
		if(StringUtil.isBlank(archives.get("categoryId"))) {
			categoryWithBLOBs = new CategoryWithBLOBs();
			categoryWithBLOBs.setCoverTemp("/index_article.html");
			categoryWithBLOBs.setListTemp("/list_article.html");
			categoryWithBLOBs.setArticleTemp("/article_article.html");
		}else {
			categoryWithBLOBs = categoryService.selectById(archives.get("categoryId").toString());
		}
		
		String imagePath = "";
		if(archives.containsKey("imagePath")) {
			imagePath = archives.get("imagePath").toString();
			imagePath = imagePath.replace("\\", "/");
		}
 		item = item.replaceAll(FieldEnum.FIELD_AUTOINDEX.getRegexp(), String.valueOf(i));
		item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), archives.get("aid").toString());
		item = item.replaceAll(FieldEnum.FIELD_TITLE.getRegexp(), StringUtil.isBlank(archives.get("title")) ? "" : archives.get("title").toString());
		item = item.replaceAll(FieldEnum.FIELD_PROPERTIES.getRegexp(), StringUtil.isBlank(archives.get("properties")) ? "" : archives.get("properties").toString());
		item = item.replaceAll(FieldEnum.FIELD_LITPIC.getRegexp(), system.getWebsite() + system.getUploaddir() + "/" + imagePath);
		item = item.replaceAll(FieldEnum.FIELD_TAG.getRegexp(), StringUtil.isBlank(archives.get("tag")) ? "" : archives.get("tag").toString());
		item = item.replaceAll(FieldEnum.FIELD_REMARK.getRegexp(), StringUtil.isBlank(archives.get("description")) ? "" : archives.get("description").toString());
		item = item.replaceAll(FieldEnum.FIELD_CATEGORYID.getRegexp(), StringUtil.isBlank(archives.get("categoryId")) ? "-1" : archives.get("categoryId").toString());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMECN.getRegexp(), StringUtil.isBlank(archives.get("categoryCnName")) ? "" : archives.get("categoryCnName").toString());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMEEN.getRegexp(), StringUtil.isBlank(archives.get("categoryEnName")) ? "-1" : archives.get("categoryEnName").toString());
		item = item.replaceAll(FieldEnum.FIELD_COMMENT.getRegexp(), StringUtil.isBlank(archives.get("comment")) ? "" : archives.get("comment").toString());
		item = item.replaceAll(FieldEnum.FIELD_SUBSCRIBE.getRegexp(), StringUtil.isBlank(archives.get("subscribe")) ? "" : archives.get("subscribe").toString());
		item = item.replaceAll(FieldEnum.FIELD_CLICKS.getRegexp(), StringUtil.isBlank(archives.get("clicks")) ? "" : archives.get("clicks").toString());
		item = item.replaceAll(FieldEnum.FIELD_WEIGHT.getRegexp(), StringUtil.isBlank(archives.get("weight")) ? "" : archives.get("weight").toString());
		item = item.replaceAll(FieldEnum.FIELD_STATUS.getRegexp(), StringUtil.isBlank(archives.get("status")) ? "" : archives.get("status").toString());
		item = item.replaceAll(FieldEnum.FIELD_CREATEBY.getRegexp(), StringUtil.isBlank(archives.get("createBy")) ? "" : archives.get("createBy").toString());
		item = item.replaceAll(FieldEnum.FIELD_CREATETIME.getRegexp(), StringUtil.isBlank(archives.get("createTime")) ? "" : archives.get("createTime").toString());
		item = item.replaceAll(FieldEnum.FIELD_UPDATEBY.getRegexp(), StringUtil.isBlank(archives.get("updateBy")) ? "" : archives.get("updateBy").toString());
		item = item.replaceAll(FieldEnum.FIELD_UPDATETIME.getRegexp(), StringUtil.isBlank(archives.get("updateTime")) ? "" : archives.get("updateTime").toString());
		
		if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
			item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), "/article/"+archives.get("aid").toString());
		}else {//生成
			String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
			Date createTime = (Date)archives.get("createTime");
			String artDate = sdf.format(createTime);
			String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + archives.get("aid").toString() + ".html";
			item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), artUrl);
		}
		//处理附加字段
		for (int k = 0; k < addfields.length; k++) {
			String field = addfields[k];
			item = item.replaceAll(FieldEnum.FIELD_ADDFIELDS_START.getRegexp() + field + FieldEnum.FIELD_ADDFIELDS_END.getRegexp(), archives.containsKey(field) ? StringUtil.isBlank(archives.get(field)) ? "" : archives.get(field).toString() : "");
		}
		return item;
	}
	
	public String buildHTML(String item, ArchivesVo archives, int i) {
		cn.itechyou.cms.entity.System system = systemService.getSystem();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		CategoryWithBLOBs categoryWithBLOBs = null;
		if(StringUtil.isBlank(archives.getCategoryId())) {
			categoryWithBLOBs = new CategoryWithBLOBs();
			categoryWithBLOBs.setCoverTemp("/index_article.html");
			categoryWithBLOBs.setListTemp("/list_article.html");
			categoryWithBLOBs.setArticleTemp("/article_article.html");
		}else {
			categoryWithBLOBs = categoryService.selectById(archives.getCategoryId());
		}
		
		String imagePath = "";
		if(StringUtil.isNotBlank(archives.getImagePath())) {
			imagePath = archives.getImagePath();
			imagePath = imagePath.replace("\\", "/");
		}
 		item = item.replaceAll(FieldEnum.FIELD_AUTOINDEX.getRegexp(), String.valueOf(i));
		item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), archives.getId());
		item = item.replaceAll(FieldEnum.FIELD_TITLE.getRegexp(), StringUtil.isBlank(archives.getTitle()) ? "" : archives.getTitle());
		item = item.replaceAll(FieldEnum.FIELD_PROPERTIES.getRegexp(), StringUtil.isBlank(archives.getProperties()) ? "" : archives.getProperties());
		item = item.replaceAll(FieldEnum.FIELD_LITPIC.getRegexp(), system.getWebsite() + system.getUploaddir() + "/" + imagePath);
		item = item.replaceAll(FieldEnum.FIELD_TAG.getRegexp(), StringUtil.isBlank(archives.getTag()) ? "" : archives.getTag());
		item = item.replaceAll(FieldEnum.FIELD_REMARK.getRegexp(), StringUtil.isBlank(archives.getDescription()) ? "" : archives.getDescription());
		item = item.replaceAll(FieldEnum.FIELD_CATEGORYID.getRegexp(), StringUtil.isBlank(archives.getCategoryId()) ? "-1" : archives.getCategoryId());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMECN.getRegexp(), StringUtil.isBlank(archives.getCategoryCnName()) ? "" : archives.getCategoryCnName());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMEEN.getRegexp(), StringUtil.isBlank(archives.getCategoryEnName()) ? "-1" : archives.getCategoryEnName());
		item = item.replaceAll(FieldEnum.FIELD_COMMENT.getRegexp(), StringUtil.isBlank(archives.getComment()) ? "" : archives.getComment() + "");
		item = item.replaceAll(FieldEnum.FIELD_SUBSCRIBE.getRegexp(), StringUtil.isBlank(archives.getSubscribe()) ? "" : archives.getSubscribe() + "");
		item = item.replaceAll(FieldEnum.FIELD_CLICKS.getRegexp(), StringUtil.isBlank(archives.getClicks()) ? "" : archives.getClicks() + "");
		item = item.replaceAll(FieldEnum.FIELD_WEIGHT.getRegexp(), StringUtil.isBlank(archives.getWeight()) ? "" : archives.getWeight() + "");
		item = item.replaceAll(FieldEnum.FIELD_STATUS.getRegexp(), StringUtil.isBlank(archives.getStatus()) ? "" : archives.getStatus() + "");
		item = item.replaceAll(FieldEnum.FIELD_CREATEBY.getRegexp(), StringUtil.isBlank(archives.getCreateBy()) ? "" : archives.getCreateBy());
		item = item.replaceAll(FieldEnum.FIELD_CREATETIME.getRegexp(), StringUtil.isBlank(archives.getCreateTime()) ? "" : archives.getCreateTime().toString());
		item = item.replaceAll(FieldEnum.FIELD_UPDATEBY.getRegexp(), StringUtil.isBlank(archives.getUpdateBy()) ? "" : archives.getUpdateBy());
		item = item.replaceAll(FieldEnum.FIELD_UPDATETIME.getRegexp(), StringUtil.isBlank(archives.getUpdateTime()) ? "" : archives.getUpdateTime().toString());
		if(StringUtil.isBlank(t) || "P".equals(t)) {//解析
			item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), "/article/" + archives.getId());
		}else {//生成
			String categoryDir = URLUtils.getCategoryDir(categoryWithBLOBs);
			Date createTime = archives.getCreateTime();
			String artDate = sdf.format(createTime);
			String artUrl = "/" + system.getStaticdir() + categoryDir + "/" + artDate + "/" + archives.getId() + ".html";
			item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), artUrl);
		}
		return item;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
	
}
