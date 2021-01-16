package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Attachment;

public interface AttachmentMapper extends BaseMapper<Attachment> {
    
    List<Attachment> queryListByPage(Map<String, Object> entity);

	Attachment selectByCode(@Param("code") String key);
}