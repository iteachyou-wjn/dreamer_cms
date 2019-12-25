package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Label;

public interface LabelMapper {
    int deleteByPrimaryKey(String id);

    int insert(Label record);

    int insertSelective(Label record);

    Label selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Label record);

    int updateByPrimaryKey(Label record);

	List<Label> selectByLabelName(@Param("tagName") String string);

	List<String> queryGroup();

	List<Map<String, String>> queryLabelByGroup(@Param("firstChar") String group);

	int deleteByTagName(String tagName);

	List<Label> queryAll();

	List<Label> queryLabelByPage(Map<String, Object> entity);
}