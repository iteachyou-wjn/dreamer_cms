package cn.itechyou.cms.taglib.enums;

import java.util.Date;

public enum FieldEnum {
	FIELD_TYPEID("typeid","(\\[field:typeid[ \\t]*.*?/\\])"),
	FIELD_TYPENAMECN("typenamecn","(\\[field:typenamecn[ \\t]*.*?/\\])"),
	FIELD_TYPENAMEEN("typenameen","(\\[field:typenameen[ \\t]*.*?/\\])"),
	FIELD_TYPECODE("typecode","(\\[field:typecode[ \\t]*.*?/\\])"),
	FIELD_TYPESEQ("typeseq","(\\[field:typeseq[ \\t]*.*?/\\])"),
	FIELD_TYPEIMG("typeimg","(\\[field:typeimg[ \\t]*.*?/\\])"),
	FIELD_DESCRIPTION("description","(\\[field:description[ \\t]*.*?/\\])"),
	FIELD_LINKTARGET("linktarget","(\\[field:linktarget[ \\t]*.*?/\\])"),
	FIELD_PAGESIZE("pagesize","(\\[field:pagesize[ \\t]*.*?/\\])"),
	FIELD_VISITURL("visiturl","(\\[field:visiturl[ \\t]*.*?/\\])"),
	FIELD_LINKURL("linkurl","(\\[field:linkurl[ \\t]*.*?/\\])"),
	FIELD_EDITOR("editor","(\\[field:editor[ \\t]*.*?/\\])"),
	FIELD_MARKDOWN("mdcontent","(\\[field:mdcontent[ \\t]*.*?/\\])"),
	FIELD_UEHTML("htmlcontent","(\\[field:htmlcontent[ \\t]*.*?/\\])"),
	FIELD_PARENTNAME("parentname","(\\[field:parentname[ \\t]*.*?/\\])"),
	FIELD_ISSHOW("isshow","(\\[field:isshow[ \\t]*.*?/\\])"),
	FIELD_LEVEL("level","(\\[field:level[ \\t]*.*?/\\])"),
	FIELD_SORT("sort","(\\[field:sort[ \\t]*.*?/\\])"),
	FIELD_TCREATEBY("createby","(\\[field:createby[ \\t]*.*?/\\])"),
	FIELD_TCREATETIME("createtime","(\\[field:createtime[ \\t]*.*?/\\])"),
	FIELD_TUPDATEBY("updateby","(\\[field:updateby[ \\t]*.*?/\\])"),
	FIELD_TUPDATETIME("updatetime","(\\[field:updatetime[ \\t]*.*?/\\])"),
	FIELD_TYPEURL("typeurl","(\\[field:typeurl[ \\t]*.*?/\\])"),
	FIELD_EXT01("ext01","(\\[field:ext01[ \\t]*.*?/\\])"),
	FIELD_EXT02("ext02","(\\[field:ext02[ \\t]*.*?/\\])"),
	FIELD_EXT03("ext03","(\\[field:ext03[ \\t]*.*?/\\])"),
	FIELD_EXT04("ext04","(\\[field:ext04[ \\t]*.*?/\\])"),
	FIELD_EXT05("ext05","(\\[field:ext05[ \\t]*.*?/\\])"),
	FIELD_HASCHILDREN("haschildren","(\\[field:haschildren[ \\t]*.*?/\\])"),
	
	CATEGORY_FIELD_START("category_start","(\\[field:"),
	CATEGORY_FIELD_END("category_end","[ \\t]*.*?/\\])"),
	
	FIELD_ID("id","(\\[field:id[ \\t]*.*?/\\])"),
	FIELD_TITLE("title","(\\[field:title[ \\t]*.*?/\\])"),
	FIELD_PROPERTIES("properties","(\\[field:properties[ \\t]*.*?/\\])"),
	FIELD_LITPIC("litpic","(\\[field:litpic[ \\t]*.*?/\\])"),
	FIELD_TAG("tag","(\\[field:tag[ \\t]*.*?/\\])"),
	FIELD_REMARK("remark","(\\[field:remark[ \\t]*.*?/\\])"),
	FIELD_CATEGORYID("categoryid","(\\[field:categoryid[ \\t]*.*?/\\])"),
	FIELD_COMMENT("comment","(\\[field:comment[ \\t]*.*?/\\])"),
	FIELD_SUBSCRIBE("subscribe","(\\[field:subscribe[ \\t]*.*?/\\])"),
	FIELD_CLICKS("clicks","(\\[field:clicks[ \\t]*.*?/\\])"),
	FIELD_WEIGHT("weight","(\\[field:weight[ \\t]*.*?/\\])"),
	FIELD_STATUS("status","(\\[field:status[ \\t]*.*?/\\])"),
	FIELD_CREATEBY("createby","(\\[field:createby[ \\t]*.*?/\\])"),
	FIELD_CREATETIME("createtime","(\\[field:createtime[ \\t]*.*?/\\])"),
	FIELD_UPDATEBY("updateby","(\\[field:updateby[ \\t]*.*?/\\])"),
	FIELD_UPDATETIME("updatetime","(\\[field:updatetime[ \\t]*.*?/\\])"),
	FIELD_ARCURL("arcurl","(\\[field:arcurl[ \\t]*.*?/\\])"),
	
	FIELD_ADDFIELDS_START("addfields_start","(\\[field:"),
	FIELD_ADDFIELDS_END("addfields_end","[ \\t]*.*?/\\])"),
	
	FIELD_AUTOINDEX("autoindex","(\\[field:autoindex[ \\t]*.*?/\\])"),
	FIELD_TAGNAME("tagname","(\\[field:tagname[ \\t]*.*?/\\])"),
	FIELD_TAGPINYIN("tagpinyin","(\\[field:tagpinyin[ \\t]*.*?/\\])"),
	FIELD_TAGFIRSTCHAR("tagfirstchar","(\\[field:tagfirstchar[ \\t]*.*?/\\])"),
	
	FIELD_FILENAME("filename","(\\[field:filename[ \\t]*.*?/\\])"),
	FIELD_FILETYPE("filetype","(\\[field:filetype[ \\t]*.*?/\\])"),
	FIELD_FILESIZE("filesize","(\\[field:filesize[ \\t]*.*?/\\])"), 
	FIELD_DLURL("dlurl","(\\[field:dlurl[ \\t]*.*?/\\])"),
	;
	private String field;
	private String regexp;
	
	private FieldEnum(String field, String regexp) {
		this.field = field;
		this.regexp = regexp;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

}
