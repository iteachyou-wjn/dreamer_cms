package cn.itechyou.cms.utils;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.itechyou.cms.entity.Theme;

/**
 * SAX解析XML 查看事件调用
 * 
 */
public class SaxUtils {
	
	public static Theme theme = null;

	public static void parseXml(String path) throws Exception {
		// step 1: 获得SAX解析器工厂实例
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// step 2: 获得SAX解析器实例
		SAXParser parser = factory.newSAXParser();

		File file = new File(path);
		if(!file.exists()) {
			return;
		}
		parser.parse(file, new ParseXmlHandler());
	}
}

class ParseXmlHandler extends DefaultHandler {
	// 使用栈这个数据结构来保存
	private Theme theme;
	private String currentTag = "";

	@Override
	public void startDocument() throws SAXException {
		theme = new Theme();
	}

	@Override
	public void endDocument() throws SAXException {
		SaxUtils.theme = this.theme;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentTag = qName;
		if("properties".equals(currentTag)) {
			String value = attributes.getValue("name");
			theme.setThemePath(value);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String value = new String(ch,start,length);
		if("name".equals(currentTag)) {
			theme.setThemeName(value);
		}else if("author".equals(currentTag)) {
			theme.setThemeAuthor(value);
		}else if("img".equals(currentTag)) {
			theme.setThemeImg(value);
		}else if("path".equals(currentTag)) {
			theme.setThemePath(value);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentTag = null;
	}
}