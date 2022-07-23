package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Form;

public interface FormMapper extends BaseMapper<Form> {

	List<Form> queryListByPage(Map<String, Object> entity);

	void createTable(Form form);

	void renameTable(Map<String, Object> params);

	void dropTable(@Param("tableName") String tableName);

	void alterTableAddColumn(@Param("sql") String sql);

	void alterTableDropColumn(@Param("sql") String sql);

	void alterTableChangeColumn(@Param("sql") String sql);

	List<Form> queryAll();

	Form queryDefaultForm();

	Form queryFormByCode(@Param("code") String value);
}