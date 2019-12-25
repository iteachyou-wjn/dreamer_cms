package cn.itechyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.AttachmentMapper;
import cn.itechyou.cms.entity.Attachment;
import cn.itechyou.cms.service.AttachmentService;

/**
 * 附件管理
 * @author Wangjn
 *
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	public PageInfo<Attachment> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Attachment> list = attachmentMapper.queryListByPage(params.getEntity());
		PageInfo<Attachment> page = new PageInfo<Attachment>(list);
		return page;
	}

	@Override
	public int save(Attachment attachment) {
		return attachmentMapper.insertSelective(attachment);
	}

	@Override
	public int delete(String id) {
		return attachmentMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Attachment queryAttachmentById(String id) {
		return attachmentMapper.selectByPrimaryKey(id);
	}

	@Override
	public Attachment queryAttachmentByCode(String key) {
		return attachmentMapper.selectByCode(key);
	}
}
