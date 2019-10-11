package cn.itechyou.cms.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Field;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.exception.TransactionException;

public interface FormService{

	PageInfo<Form> queryListByPage(SearchEntity params);

	int add(Form form) throws TransactionException;

	void createTable(Form form);

	Form queryFormById(String id);

	int update(Form newForm, Form oldForm) throws TransactionException;

	void renameTable(Map<String, Object> params);

	int delete(Form form,List<Field> fields) throws TransactionException;

	void dropTable(String tableName);

	void alterTableAddColumn(String sql);

	void alterTableDropColumn(String string);

	void alterTableChangeColumn(String string);

	List<Form> queryAll();

	Form queryDefaultForm();

	Form queryFormByCode(String value);

}
