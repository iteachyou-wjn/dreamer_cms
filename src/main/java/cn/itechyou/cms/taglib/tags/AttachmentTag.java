package cn.itechyou.cms.taglib.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itechyou.cms.entity.Attachment;
import cn.itechyou.cms.service.AttachmentService;
import cn.itechyou.cms.taglib.IParse;
import cn.itechyou.cms.taglib.annotation.Attribute;
import cn.itechyou.cms.taglib.annotation.Tag;
import cn.itechyou.cms.taglib.enums.FieldEnum;
import cn.itechyou.cms.taglib.utils.RegexUtil;
import cn.itechyou.cms.utils.StringUtil;

/**
 * Attachment标签解析
 * @author Wangjn
 * @version 1.0.0
 */
@Component
@Tag(beginTag="{dreamer-cms:attachment}",endTag="{/dreamer-cms:attachment}",regexp="\\{dreamer-cms:attachment[ \\t]*.*\\}([\\s\\S]+?)\\{/dreamer-cms:attachment\\}", attributes={
		@Attribute(name = "key",regex = "[ \t]+key=[\"\'].*?[\"\']"),
	})
public class AttachmentTag implements IParse {

	@Autowired
	private AttachmentService attachmentService;
	
	@Override
	public String parse(String html) {
		Tag annotations = AttachmentTag.class.getAnnotation(Tag.class);
		List<String> attachmentTags = RegexUtil.parseAll(html, annotations.regexp(), 0);
		List<String> contents = RegexUtil.parseAll(html, annotations.regexp(), 1);
		
		if(attachmentTags == null || attachmentTags.size() <= 0) {
			return html;
		}
		
		Attribute[] attributes = annotations.attributes();
		
		String newHtml = html;
		for (int i = 0;i < attachmentTags.size();i++) {
			String tag = attachmentTags.get(i);
			String content = contents.get(i);
			
			Map<String,Object> entity = new HashMap<String,Object>();
			for (Attribute attribute : attributes) {
				String condition = RegexUtil.parseFirst(tag, attribute.regex(), 0);
				if(StringUtil.isBlank(condition)) {
					continue;
				}
				String key = condition.split("=")[0];
				String value = condition.split("=")[1];
				key = key.trim();
				value = value.replace("\"", "").replace("\'", "");
				entity.put(key, value);
			}
			
			if(!entity.containsKey("key")) {
				
			}
			String key = entity.get("key").toString();
			Attachment attachment = attachmentService.queryAttachmentByCode(key);
			
			String item = new String(content);
			item = item.replaceAll(FieldEnum.FIELD_ID.getRegexp(), attachment.getId());
			item = item.replaceAll(FieldEnum.FIELD_FILENAME.getRegexp(), attachment.getFilename());
			item = item.replaceAll(FieldEnum.FIELD_FILETYPE.getRegexp(), attachment.getFiletype());
			item = item.replaceAll(FieldEnum.FIELD_FILESIZE.getRegexp(), StringUtil.isBlank(attachment.getFilesize()) ? "" : String.valueOf(attachment.getFilesize()));
			item = item.replaceAll(FieldEnum.FIELD_CREATEBY.getRegexp(), attachment.getCreateBy());
			item = item.replaceAll(FieldEnum.FIELD_CREATETIME.getRegexp(), StringUtil.isBlank(attachment.getCreateTime()) ? "" : attachment.getCreateTime().toString());
			item = item.replaceAll(FieldEnum.FIELD_DLURL.getRegexp(), "/download/" + attachment.getId());
			newHtml = newHtml.replace(tag, item);		
		}
		return newHtml;
	}

	@Override
	public String parse(String html, String params) {
		return null;
	}

}
