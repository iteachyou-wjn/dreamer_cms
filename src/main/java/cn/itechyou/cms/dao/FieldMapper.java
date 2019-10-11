package cn.itechyou.cms.dao;

import java.util.List;

import cn.itechyou.cms.entity.Field;

public interface FieldMapper {
    int deleteByPrimaryKey(String id);

    int insert(Field record);

    int insertSelective(Field record);

    Field selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Field record);

    int updateByPrimaryKey(Field record);

	List<Field> queryFieldByFormId(String id);
}