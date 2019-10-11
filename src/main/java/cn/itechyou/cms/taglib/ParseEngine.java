package cn.itechyou.cms.taglib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.taglib.tags.ArticleTag;
import cn.itechyou.cms.taglib.tags.CategoryTag;
import cn.itechyou.cms.taglib.tags.ChannelArtListTag;
import cn.itechyou.cms.taglib.tags.ChannelTag;
import cn.itechyou.cms.taglib.tags.GlobalTag;
import cn.itechyou.cms.taglib.tags.IfTag;
import cn.itechyou.cms.taglib.tags.IncludeTag;
import cn.itechyou.cms.taglib.tags.ListTag;
import cn.itechyou.cms.taglib.tags.PageListTag;
import cn.itechyou.cms.taglib.tags.TemplateTag;
import cn.itechyou.cms.taglib.tags.TypeTag;
import cn.itechyou.cms.taglib.tags.VariableTag;

@Component
public class ParseEngine {
	
	@Autowired
	private IncludeTag includeTag;//Include标签解析器
	@Autowired
	private GlobalTag globalTag;
	@Autowired
	private TemplateTag templateTag;
	@Autowired
	private ChannelArtListTag channelArtListTag;
	@Autowired
	private ChannelTag channelTag;
	@Autowired
	private ListTag listTag;
	@Autowired
	private TypeTag typeTag;
	@Autowired 
	private IfTag ifTag;
	@Autowired
	private VariableTag variableTag;
	@Autowired
	private CategoryTag categoryTag;
	@Autowired
	private PageListTag pageListTag;
	@Autowired
	private ArticleTag articleTag;
	
	/**
	 * HTML解析入口
	 * @param html
	 * @return
	 */
	public String parse(String html){
		String newHtml = new String(html);
		newHtml = includeTag.parse(newHtml);
		newHtml = globalTag.parse(newHtml);
		newHtml = variableTag.parse(newHtml);
		newHtml = templateTag.parse(newHtml);
		newHtml = typeTag.parse(newHtml);
		newHtml = channelArtListTag.parse(newHtml);
		newHtml = channelTag.parse(newHtml);
		newHtml = listTag.parse(newHtml);
		newHtml = ifTag.parse(newHtml);
		return newHtml;
	}
	
	/**
	 * 栏目页解析
	 * @param newHtml
	 * @return
	 */
	public String parseCategory(String html, String typeid) {
		String newHtml = new String(html);
		newHtml = categoryTag.parse(newHtml, typeid);
		return newHtml;
	}

	public String parsePageList(String html, String typeid, Integer pageNum, Integer pageSize) {
		String newHtml = new String(html);
		newHtml = pageListTag.parse(newHtml,typeid,pageNum,pageSize);
		return newHtml;
	}

	public String parseArticle(String html, String id) {
		String newHtml = new String(html);
		newHtml = articleTag.parse(newHtml, id);
		return newHtml;
	}

}
