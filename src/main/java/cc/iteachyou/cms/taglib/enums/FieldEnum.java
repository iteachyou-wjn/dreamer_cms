package cc.iteachyou.cms.taglib.enums;

public enum FieldEnum {
	FIELD_TYPEID("typeid","(\\[field:typeid[\\s]+.*?/\\])"),
	FIELD_TYPENAMECN("typenamecn","(\\[field:typenamecn([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_TYPENAMEEN("typenameen","(\\[field:typenameen([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_TYPECODE("typecode","(\\[field:typecode[\\s]*?/\\])"),
	FIELD_TYPESEQ("typeseq","(\\[field:typeseq[\\s]*?/\\])"),
	FIELD_TYPEIMG("typeimg","(\\[field:typeimg[\\s]*?/\\])"),
	FIELD_DESCRIPTION("description","(\\[field:description[\\s]*?/\\])"),
	FIELD_LINKTARGET("linktarget","(\\[field:linktarget[\\s]*?/\\])"),
	FIELD_PAGESIZE("pagesize","(\\[field:pagesize[\\s]*?/\\])"),
	FIELD_VISITURL("visiturl","(\\[field:visiturl[\\s]*?/\\])"),
	FIELD_LINKURL("linkurl","(\\[field:linkurl[\\s]*?/\\])"),
	FIELD_EDITOR("editor","(\\[field:editor[\\s]*?/\\])"),
	FIELD_MARKDOWN("mdcontent","(\\[field:mdcontent[\\s]*?/\\])"),
	FIELD_UEHTML("htmlcontent","(\\[field:htmlcontent[\\s]*?/\\])"),
	FIELD_PARENTID("parentid","(\\[field:parentid[\\s]*?/\\])"),
	FIELD_PARENTNAME("parentname","(\\[field:parentname[\\s]*?/\\])"),
	FIELD_ISSHOW("isshow","(\\[field:isshow[\\s]*?/\\])"),
	FIELD_LEVEL("level","(\\[field:level[\\s]*?/\\])"),
	FIELD_SORT("sort","(\\[field:sort[\\s]*?/\\])"),
	FIELD_TCREATEBY("createby","(\\[field:createby[\\s]*?/\\])"),
	FIELD_TCREATETIME("createtime","(\\[field:createtime([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_TUPDATEBY("updateby","(\\[field:updateby[\\s]+.*?/\\])"),
	FIELD_TUPDATETIME("updatetime","(\\[field:updatetime([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_TYPEURL("typeurl","(\\[field:typeurl[\\s]*?/\\])"),
	FIELD_EXT01("ext01","(\\[field:ext01[\\s]*?/\\])"),
	FIELD_EXT02("ext02","(\\[field:ext02[\\s]*?/\\])"),
	FIELD_EXT03("ext03","(\\[field:ext03[\\s]*?/\\])"),
	FIELD_EXT04("ext04","(\\[field:ext04[\\s]*?/\\])"),
	FIELD_EXT05("ext05","(\\[field:ext05[\\s]*?/\\])"),
	FIELD_HASCHILDREN("haschildren","(\\[field:haschildren[\\s]*?/\\])"),
	
	FIELD_ID("id","(\\[field:id[\\s]*?/\\])"),
	FIELD_TITLE("title","(\\[field:title([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_PROPERTIES("properties","(\\[field:properties[\\s]*?/\\])"),
	FIELD_LITPIC("litpic","(\\[field:litpic[\\s]*?/\\])"),
	FIELD_HASTHUMBNAIL("hasthumbnail","(\\[field:hasthumbnail[\\s]*?/\\])"),
	FIELD_TAG("tag","(\\[field:tag[\\s]*?/\\])"),
	FIELD_REMARK("remark","(\\[field:remark[\\s]*?/\\])"),
	FIELD_CATEGORYID("categoryid","(\\[field:categoryid[\\s]*?/\\])"),
	FIELD_COMMENT("comment","(\\[field:comment[\\s]*?/\\])"),
	FIELD_SUBSCRIBE("subscribe","(\\[field:subscribe[\\s]*?/\\])"),
	FIELD_CLICKS("clicks","(\\[field:clicks[\\s]*?/\\])"),
	FIELD_WEIGHT("weight","(\\[field:weight[\\s]*?/\\])"),
	FIELD_STATUS("status","(\\[field:status[\\s]*?/\\])"),
	FIELD_CREATEBY("createby","(\\[field:createby[\\s]*?/\\])"),
	FIELD_CREATEUSERNAME("username","(\\[field:username[\\s]*?/\\])"),
	FIELD_CREATEREALNAME("realname","(\\[field:realname[\\s]*?/\\])"),
	FIELD_CREATETIME("createtime","(\\[field:createtime([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_UPDATEBY("updateby","(\\[field:updateby[\\s]*?/\\])"),
	FIELD_UPDATETIME("updatetime","(\\[field:updatetime([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_ARCURL("arcurl","(\\[field:arcurl[\\s]*?/\\])"),

	FIELD_ADDFIELDS_START("addfields_start","(\\[field:"),
	FIELD_ADDFIELDS_END("addfields_end","[\\s]*?/\\])"),
	
	FIELD_AUTOINDEX("autoindex","(\\[field:autoindex([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_TAGNAME("tagname","(\\[field:tagname[\\s]*?/\\])"),
	FIELD_TAGPINYIN("tagpinyin","(\\[field:tagpinyin[\\s]*?/\\])"),
	FIELD_TAGFIRSTCHAR("tagfirstchar","(\\[field:tagfirstchar[\\s]*?/\\])"),
	
	FIELD_FILENAME("filename","(\\[field:filename([\\s]+function=\\\"((.*?)\\((.*?)\\)?)\\\"){0,1}[\\s]*?/\\])"),
	FIELD_FILETYPE("filetype","(\\[field:filetype[\\s]*?/\\])"),
	FIELD_FILESIZE("filesize","(\\[field:filesize[\\s]*?/\\])"), 
	FIELD_DLURL("dlurl","(\\[field:dlurl[\\s]*?/\\])"),
	
	FUNCTION("function", "function=[\\\"|'](.*?)\\((.*?)\\)[\\\"|']"),
	FUNCTION_ARTICLE("function","((.*?)\\((.*?)\\))"),
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
