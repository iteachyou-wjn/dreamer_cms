package cn.itechyou.cms.taglib.tags;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.utils.StringUtils;

/**
 * 列表标签抽象类
 * @author Wangjn
 *
 */
public abstract class AbstractListTag {
	@Autowired
	private SystemService systemService;
	
	public String buildHTML(String item, Map<String, Object> archivesVo, String[] addfields, int i) {
		cn.itechyou.cms.entity.System system = systemService.getSystem();
		
		String imagePath = "";
		if(archivesVo.containsKey("imagePath")) {
			imagePath = archivesVo.get("imagePath").toString();
			imagePath = imagePath.replace("\\", "/");
		}
 		item = item.replaceAll(FieldEnum.FIELD_AUTOINDEX.getRegexp(), String.valueOf(i));
		item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), archivesVo.get("aid").toString());
		item = item.replaceAll(FieldEnum.FIELD_TITLE.getRegexp(), StringUtils.isBlank(archivesVo.get("title")) ? "" : archivesVo.get("title").toString());
		item = item.replaceAll(FieldEnum.FIELD_PROPERTIES.getRegexp(), StringUtils.isBlank(archivesVo.get("properties")) ? "" : archivesVo.get("properties").toString());
		item = item.replaceAll(FieldEnum.FIELD_LITPIC.getRegexp(), system.getWebsite() + system.getUploaddir() + "/" + imagePath);
		item = item.replaceAll(FieldEnum.FIELD_TAG.getRegexp(), StringUtils.isBlank(archivesVo.get("tag")) ? "" : archivesVo.get("tag").toString());
		item = item.replaceAll(FieldEnum.FIELD_REMARK.getRegexp(), StringUtils.isBlank(archivesVo.get("description")) ? "" : archivesVo.get("description").toString());
		item = item.replaceAll(FieldEnum.FIELD_CATEGORYID.getRegexp(), StringUtils.isBlank(archivesVo.get("categoryId")) ? "-1" : archivesVo.get("categoryId").toString());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMECN.getRegexp(), StringUtils.isBlank(archivesVo.get("categoryCnName")) ? "" : archivesVo.get("categoryCnName").toString());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMEEN.getRegexp(), StringUtils.isBlank(archivesVo.get("categoryEnName")) ? "-1" : archivesVo.get("categoryEnName").toString());
		item = item.replaceAll(FieldEnum.FIELD_COMMENT.getRegexp(), StringUtils.isBlank(archivesVo.get("comment")) ? "" : archivesVo.get("comment").toString());
		item = item.replaceAll(FieldEnum.FIELD_SUBSCRIBE.getRegexp(), StringUtils.isBlank(archivesVo.get("subscribe")) ? "" : archivesVo.get("subscribe").toString());
		item = item.replaceAll(FieldEnum.FIELD_CLICKS.getRegexp(), StringUtils.isBlank(archivesVo.get("clicks")) ? "" : archivesVo.get("clicks").toString());
		item = item.replaceAll(FieldEnum.FIELD_WEIGHT.getRegexp(), StringUtils.isBlank(archivesVo.get("weight")) ? "" : archivesVo.get("weight").toString());
		item = item.replaceAll(FieldEnum.FIELD_STATUS.getRegexp(), StringUtils.isBlank(archivesVo.get("status")) ? "" : archivesVo.get("status").toString());
		item = item.replaceAll(FieldEnum.FIELD_CREATEBY.getRegexp(), StringUtils.isBlank(archivesVo.get("createBy")) ? "" : archivesVo.get("createBy").toString());
		item = item.replaceAll(FieldEnum.FIELD_CREATETIME.getRegexp(), StringUtils.isBlank(archivesVo.get("createTime")) ? "" : archivesVo.get("createTime").toString());
		item = item.replaceAll(FieldEnum.FIELD_UPDATEBY.getRegexp(), StringUtils.isBlank(archivesVo.get("updateBy")) ? "" : archivesVo.get("updateBy").toString());
		item = item.replaceAll(FieldEnum.FIELD_UPDATETIME.getRegexp(), StringUtils.isBlank(archivesVo.get("updateTime")) ? "" : archivesVo.get("updateTime").toString());
		item = item.replaceAll(FieldEnum.FIELD_ARCURL.getRegexp(), "/article/"+archivesVo.get("aid").toString());
		//处理附加字段
		for (int k = 0; k < addfields.length; k++) {
			String field = addfields[k];
			item = item.replaceAll(FieldEnum.FIELD_ADDFIELDS_START.getRegexp() + field + FieldEnum.FIELD_ADDFIELDS_END.getRegexp(), archivesVo.containsKey(field) ? archivesVo.get(field).toString() : "");
		}
		return item;
	}
}
