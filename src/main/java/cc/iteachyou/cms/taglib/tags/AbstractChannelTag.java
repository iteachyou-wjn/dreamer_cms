package cc.iteachyou.cms.taglib.tags;

import org.springframework.beans.factory.annotation.Autowired;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.taglib.enums.FieldEnum;
import cc.iteachyou.cms.taglib.utils.FunctionUtil;
import cc.iteachyou.cms.taglib.utils.URLUtils;
import cc.iteachyou.cms.utils.PinyinUtils;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * 栏目标签抽象类
 * @author Wangjn
 *
 */
public abstract class AbstractChannelTag {
	
	/**
	 * 执行类型：
	 * P：解析
	 * S：生成静态化
	 */
	private String t;
	
	@Autowired
	private SystemService systemService;
	
	public String buildHTML(String item, Category category, int i) throws CmsException {
		cc.iteachyou.cms.entity.System system = systemService.getSystem();
		String imagePath = "";
		if(StringUtil.isNotBlank(category.getImagePath())) {
			imagePath = category.getImagePath();
			imagePath = imagePath.replace("\\", "/");
		}
		String typeCode = StringUtil.isBlank(category.getCode()) ? "" : category.getCode();
		String visitUrl = StringUtil.isBlank(category.getVisitUrl()) ? PinyinUtils.toPinyin(category.getCnname()) : category.getVisitUrl();
		if(!visitUrl.startsWith("/")) {
			visitUrl = "/" + visitUrl;
		}
		item = FunctionUtil.replaceByFunction(item, FieldEnum.FIELD_AUTOINDEX.getRegexp(), i);
		item = item.replaceAll(FieldEnum.FIELD_TYPEID.getRegexp(), category.getId());
		
		item = FunctionUtil.replaceByFunction(item, FieldEnum.FIELD_TYPENAMECN.getRegexp(), StringUtil.isBlank(category.getCnname()) ? "" : category.getCnname());
		item = FunctionUtil.replaceByFunction(item, FieldEnum.FIELD_TYPENAMEEN.getRegexp(), StringUtil.isBlank(category.getEnname()) ? "" : category.getEnname());
		item = item.replaceAll(FieldEnum.FIELD_TYPECODE.getRegexp(), typeCode);
		item = item.replaceAll(FieldEnum.FIELD_TYPESEQ.getRegexp(), StringUtil.isBlank(category.getCatSeq()) ? "" : category.getCatSeq());
		item = item.replaceAll(FieldEnum.FIELD_TYPEIMG.getRegexp(), system.getWebsite() + Constant.UPLOAD_PREFIX + system.getUploaddir() + "/" + imagePath);
		item = item.replaceAll(FieldEnum.FIELD_DESCRIPTION.getRegexp(), StringUtil.isBlank(category.getDescription()) ? "" : category.getDescription());
		item = item.replaceAll(FieldEnum.FIELD_LINKTARGET.getRegexp(), StringUtil.isBlank(category.getLinkTarget()) ? "" : category.getLinkTarget());
		item = item.replaceAll(FieldEnum.FIELD_PAGESIZE.getRegexp(), StringUtil.isBlank(category.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : category.getPageSize().toString());
		item = item.replaceAll(FieldEnum.FIELD_VISITURL.getRegexp(), visitUrl);
		item = item.replaceAll(FieldEnum.FIELD_LINKURL.getRegexp(), StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl());
		item = item.replaceAll(FieldEnum.FIELD_EDITOR.getRegexp(), StringUtil.isBlank(category.getDefaultEditor()) ? "" : category.getDefaultEditor());
		item = item.replaceAll(FieldEnum.FIELD_MARKDOWN.getRegexp(), StringUtil.isBlank(category.getMdContent()) ? "" : category.getMdContent());
		item = item.replaceAll(FieldEnum.FIELD_UEHTML.getRegexp(), StringUtil.isBlank(category.getHtmlContent()) ? "" : category.getHtmlContent());
		item = item.replaceAll(FieldEnum.FIELD_PARENTID.getRegexp(), StringUtil.isBlank(category.getParentId()) ? "" : category.getParentId());
		item = item.replaceAll(FieldEnum.FIELD_PARENTNAME.getRegexp(), StringUtil.isBlank(category.getParentName()) ? "" : category.getParentName());
		item = item.replaceAll(FieldEnum.FIELD_ISSHOW.getRegexp(), StringUtil.isBlank(category.getIsShow()) ? "" : category.getIsShow().toString());
		item = item.replaceAll(FieldEnum.FIELD_LEVEL.getRegexp(), StringUtil.isBlank(category.getLevel()) ? "" : category.getLevel());
		item = item.replaceAll(FieldEnum.FIELD_SORT.getRegexp(), StringUtil.isBlank(category.getSort()) ? "" : category.getSort().toString());
		item = item.replaceAll(FieldEnum.FIELD_TCREATEBY.getRegexp(), StringUtil.isBlank(category.getCreateBy()) ? "" : category.getCreateBy());
		item = FunctionUtil.replaceByFunction(item, FieldEnum.FIELD_TCREATETIME.getRegexp(), StringUtil.isBlank(category.getCreateTime()) ? "" : category.getCreateTime());
		item = item.replaceAll(FieldEnum.FIELD_TUPDATEBY.getRegexp(), StringUtil.isBlank(category.getUpdateBy()) ? "" : category.getUpdateBy());
		item = FunctionUtil.replaceByFunction(item, FieldEnum.FIELD_TUPDATETIME.getRegexp(), StringUtil.isBlank(category.getUpdateTime()) ? "" : category.getUpdateTime());
		String typeUrl = URLUtils.parseURL(system, category, this.t);
		item = item.replaceAll(FieldEnum.FIELD_TYPEURL.getRegexp(), typeUrl);
		item = item.replaceAll(FieldEnum.FIELD_EXT01.getRegexp(), StringUtil.isBlank(category.getExt01()) ? "" : category.getExt01());
		item = item.replaceAll(FieldEnum.FIELD_EXT02.getRegexp(), StringUtil.isBlank(category.getExt02()) ? "" : category.getExt02());
		item = item.replaceAll(FieldEnum.FIELD_EXT03.getRegexp(), StringUtil.isBlank(category.getExt03()) ? "" : category.getExt03());
		item = item.replaceAll(FieldEnum.FIELD_EXT04.getRegexp(), StringUtil.isBlank(category.getExt04()) ? "" : category.getExt04());
		item = item.replaceAll(FieldEnum.FIELD_EXT05.getRegexp(), StringUtil.isBlank(category.getExt05()) ? "" : category.getExt05());
		item = item.replaceAll(FieldEnum.FIELD_HASCHILDREN.getRegexp(), (category.getNodes() == null || category.getNodes().size() <= 0) ? "false" : "true");
		return item;
	}
	
	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
	
}
