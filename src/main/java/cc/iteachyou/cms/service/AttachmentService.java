package cc.iteachyou.cms.service;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Attachment;

/**
 * 附件管理
 * @author Wangjn
 *
 */
public interface AttachmentService {

	PageInfo<Attachment> queryListByPage(SearchEntity params);

	int save(Attachment attachment);

	int delete(String id);

	Attachment queryAttachmentById(String id);

	Attachment queryAttachmentByCode(String key);

}
