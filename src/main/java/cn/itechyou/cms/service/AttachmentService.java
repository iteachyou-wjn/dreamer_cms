package cn.itechyou.cms.service;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Attachment;

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
