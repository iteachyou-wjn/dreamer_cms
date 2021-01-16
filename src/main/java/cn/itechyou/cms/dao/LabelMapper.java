package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Label;

public interface LabelMapper extends BaseMapper<Label> {

	List<Label> selectByLabelName(@Param("tagName") String string);

	List<String> queryGroup();

	List<Map<String, String>> queryLabelByGroup(@Param("firstChar") String group);

	int deleteByTagName(String tagName);

	List<Label> queryAll();

	List<Label> queryLabelByPage(Map<String, Object> entity);
}