package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Attachment;
import cn.itechyou.cms.entity.Variable;

public interface AttachmentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);
    
    List<Attachment> queryListByPage(Map<String, Object> entity);

	Attachment selectByCode(@Param("code") String key);
}