package cn.itechyou.cms.dao;

import java.util.List;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Field;

public interface FieldMapper extends BaseMapper<Field> {

	List<Field> queryFieldByFormId(String id);
}