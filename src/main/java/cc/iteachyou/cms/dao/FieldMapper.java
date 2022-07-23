package cc.iteachyou.cms.dao;

import java.util.List;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Field;

public interface FieldMapper extends BaseMapper<Field> {

	List<Field> queryFieldByFormId(String id);
}