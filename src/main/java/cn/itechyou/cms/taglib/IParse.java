package cn.itechyou.cms.taglib;

import cn.itechyou.cms.exception.CmsException;

public interface IParse {
	public String parse(String html) throws CmsException;
	
	public String parse(String html, String params) throws CmsException;
}
