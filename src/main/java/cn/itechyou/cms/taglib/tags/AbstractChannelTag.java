package cn.itechyou.cms.taglib.tags;

import org.springframework.beans.factory.annotation.Autowired;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.StringUtil;

/**
 * 栏目标签抽象类
 * @author Wangjn
 *
 */
public abstract class AbstractChannelTag {
	
	@Autowired
	private SystemService systemService;
	
	public String buildHTML(String item, CategoryWithBLOBs category, int i) {
		cn.itechyou.cms.entity.System system = systemService.getSystem();
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
		item = item.replaceAll(FieldEnum.FIELD_AUTOINDEX.getRegexp(), String.valueOf(i));
		item = item.replaceAll(FieldEnum.FIELD_TYPEID.getRegexp(), category.getId());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMECN.getRegexp(), StringUtil.isBlank(category.getCnname()) ? "" : category.getCnname());
		item = item.replaceAll(FieldEnum.FIELD_TYPENAMEEN.getRegexp(), StringUtil.isBlank(category.getEnname()) ? "" : category.getEnname());
		item = item.replaceAll(FieldEnum.FIELD_TYPECODE.getRegexp(), typeCode);
		item = item.replaceAll(FieldEnum.FIELD_TYPESEQ.getRegexp(), StringUtil.isBlank(category.getCatSeq()) ? "" : category.getCatSeq());
		item = item.replaceAll(FieldEnum.FIELD_TYPEIMG.getRegexp(), system.getWebsite() + system.getUploaddir() + "/" + imagePath);
		item = item.replaceAll(FieldEnum.FIELD_DESCRIPTION.getRegexp(), StringUtil.isBlank(category.getDescription()) ? "" : category.getDescription());
		item = item.replaceAll(FieldEnum.FIELD_LINKTARGET.getRegexp(), StringUtil.isBlank(category.getLinkTarget()) ? "" : category.getLinkTarget());
		item = item.replaceAll(FieldEnum.FIELD_PAGESIZE.getRegexp(), StringUtil.isBlank(category.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : category.getPageSize().toString());
		item = item.replaceAll(FieldEnum.FIELD_VISITURL.getRegexp(), visitUrl);
		item = item.replaceAll(FieldEnum.FIELD_LINKURL.getRegexp(), StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl());
		item = item.replaceAll(FieldEnum.FIELD_EDITOR.getRegexp(), StringUtil.isBlank(category.getDefaultEditor()) ? "" : category.getDefaultEditor());
		item = item.replaceAll(FieldEnum.FIELD_MARKDOWN.getRegexp(), StringUtil.isBlank(category.getMdContent()) ? "" : category.getMdContent());
		item = item.replaceAll(FieldEnum.FIELD_UEHTML.getRegexp(), StringUtil.isBlank(category.getHtmlContent()) ? "" : category.getHtmlContent());
		item = item.replaceAll(FieldEnum.FIELD_PARENTNAME.getRegexp(), StringUtil.isBlank(category.getParentName()) ? "" : category.getParentName());
		item = item.replaceAll(FieldEnum.FIELD_ISSHOW.getRegexp(), StringUtil.isBlank(category.getIsShow()) ? "" : category.getIsShow().toString());
		item = item.replaceAll(FieldEnum.FIELD_LEVEL.getRegexp(), StringUtil.isBlank(category.getLevel()) ? "" : category.getLevel());
		item = item.replaceAll(FieldEnum.FIELD_SORT.getRegexp(), StringUtil.isBlank(category.getSort()) ? "" : category.getSort().toString());
		item = item.replaceAll(FieldEnum.FIELD_TCREATEBY.getRegexp(), StringUtil.isBlank(category.getCreateBy()) ? "" : category.getCreateBy());
		item = item.replaceAll(FieldEnum.FIELD_TCREATETIME.getRegexp(), StringUtil.isBlank(category.getCreateTime()) ? "" : category.getCreateTime().toString());
		item = item.replaceAll(FieldEnum.FIELD_TUPDATEBY.getRegexp(), StringUtil.isBlank(category.getUpdateBy()) ? "" : category.getUpdateBy());
		item = item.replaceAll(FieldEnum.FIELD_TUPDATETIME.getRegexp(), StringUtil.isBlank(category.getUpdateTime()) ? "" : category.getUpdateTime().toString());
		if(category.getCatModel() == 1) {
			item = item.replaceAll(FieldEnum.FIELD_TYPEURL.getRegexp(), "/cover-" + typeCode + visitUrl);
		}else if(category.getCatModel() == 2) {
			item = item.replaceAll(FieldEnum.FIELD_TYPEURL.getRegexp(), "/list-" + typeCode + visitUrl + "/1/"
					+ (StringUtil.isBlank(category.getPageSize()) ? Constant.PAGE_SIZE_VALUE + "" : category.getPageSize().toString()));
		}else if(category.getCatModel() == 3) {
			item = item.replaceAll(FieldEnum.FIELD_TYPEURL.getRegexp(), StringUtil.isBlank(category.getLinkUrl()) ? "" : category.getLinkUrl());
		}
		item = item.replaceAll(FieldEnum.FIELD_EXT01.getRegexp(), StringUtil.isBlank(category.getExt01()) ? "" : category.getExt01());
		item = item.replaceAll(FieldEnum.FIELD_EXT02.getRegexp(), StringUtil.isBlank(category.getExt02()) ? "" : category.getExt02());
		item = item.replaceAll(FieldEnum.FIELD_EXT03.getRegexp(), StringUtil.isBlank(category.getExt03()) ? "" : category.getExt03());
		item = item.replaceAll(FieldEnum.FIELD_EXT04.getRegexp(), StringUtil.isBlank(category.getExt04()) ? "" : category.getExt04());
		item = item.replaceAll(FieldEnum.FIELD_EXT05.getRegexp(), StringUtil.isBlank(category.getExt05()) ? "" : category.getExt05());
		item = item.replaceAll(FieldEnum.FIELD_HASCHILDREN.getRegexp(), (category.getNodes() == null || category.getNodes().size() <= 0) ? "false" : "true");
		return item;
	}
}
