package cn.itechyou.cms.taglib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.taglib.tags.ArticleTag;
import cn.itechyou.cms.taglib.tags.AttachmentTag;
import cn.itechyou.cms.taglib.tags.CategoryTag;
import cn.itechyou.cms.taglib.tags.ChannelArtListTag;
import cn.itechyou.cms.taglib.tags.ChannelTag;
import cn.itechyou.cms.taglib.tags.GlobalTag;
import cn.itechyou.cms.taglib.tags.IfTag;
import cn.itechyou.cms.taglib.tags.IncludeTag;
import cn.itechyou.cms.taglib.tags.LabelTag;
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
	private LabelTag labelTag;
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
	@Autowired
	private AttachmentTag attachmentTag;
	
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
		typeTag.setT("P");
		newHtml = typeTag.parse(newHtml);
		channelArtListTag.setT("P");
		newHtml = channelArtListTag.parse(newHtml);
		channelTag.setT("P");
		newHtml = channelTag.parse(newHtml);
		listTag.setT("P");
		newHtml = listTag.parse(newHtml);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
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
		categoryTag.setT("P");
		newHtml = categoryTag.parse(newHtml, typeid);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}
	
	/**
	 * 列表页面解析
	 * @param html
	 * @param typeid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public String parsePageList(String html, String typeid, Integer pageNum, Integer pageSize) {
		String newHtml = new String(html);
		pageListTag.setT("P");
		newHtml = pageListTag.parse(newHtml,typeid,pageNum,pageSize);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}
	
	/**
	 * 文章详情页面解析
	 * @param html
	 * @param id
	 * @return
	 */
	public String parseArticle(String html, String id) {
		String newHtml = new String(html);
		newHtml = articleTag.parse(newHtml, id);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}
	
	/**
	 * 列表页面解析
	 * @param html
	 * @param typeid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public String parsePageList(String html, SearchEntity params) {
		String newHtml = new String(html);
		pageListTag.setT("P");
		newHtml = pageListTag.parse(newHtml, params);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}

	public String generate(String html) {
		String newHtml = new String(html);
		newHtml = includeTag.parse(newHtml);
		newHtml = globalTag.parse(newHtml);
		newHtml = variableTag.parse(newHtml);
		newHtml = templateTag.parse(newHtml);
		typeTag.setT("S");
		newHtml = typeTag.parse(newHtml);
		channelArtListTag.setT("S");
		newHtml = channelArtListTag.parse(newHtml);
		channelTag.setT("S");
		newHtml = channelTag.parse(newHtml);
		listTag.setT("S");
		newHtml = listTag.parse(newHtml);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		newHtml = ifTag.parse(newHtml);
		return newHtml;
	}
	
	/**
	 * 栏目页解析
	 * @param newHtml
	 * @return
	 */
	public String generateCategory(String html, String typeid) {
		String newHtml = new String(html);
		categoryTag.setT("S");
		newHtml = categoryTag.parse(newHtml, typeid);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}

	public String generatePageList(String html, String typeid, int pageNum, int pageSize) {
		String newHtml = new String(html);
		pageListTag.setT("S");
		newHtml = pageListTag.parse(newHtml,typeid, pageNum,pageSize);
		newHtml = labelTag.parse(newHtml);
		newHtml = attachmentTag.parse(newHtml);
		return newHtml;
	}

}
