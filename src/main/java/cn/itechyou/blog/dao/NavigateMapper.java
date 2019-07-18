package cn.itechyou.blog.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.blog.entity.Navigate;

public interface NavigateMapper {
    int deleteByPrimaryKey(String id);

    int insert(Navigate record);

    int insertSelective(Navigate record);

    Navigate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Navigate record);

    int updateByPrimaryKey(Navigate record);

	List<Navigate> queryListByPage(Map<String, Object> entity);

	Navigate queryNavigateByCode(String code);
}