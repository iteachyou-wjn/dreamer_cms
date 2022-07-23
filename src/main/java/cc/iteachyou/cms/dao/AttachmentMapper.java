package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Attachment;

public interface AttachmentMapper extends BaseMapper<Attachment> {
    
    List<Attachment> queryListByPage(Map<String, Object> entity);

	Attachment selectByCode(@Param("code") String key);
}