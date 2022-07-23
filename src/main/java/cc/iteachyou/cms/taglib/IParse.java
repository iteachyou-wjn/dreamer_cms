package cc.iteachyou.cms.taglib;

import cc.iteachyou.cms.exception.CmsException;

public interface IParse {
	public String parse(String html) throws CmsException;
	
	public String parse(String html, String params) throws CmsException;
}
