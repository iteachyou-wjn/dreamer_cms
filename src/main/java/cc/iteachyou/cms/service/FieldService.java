package cc.iteachyou.cms.service;

import java.util.List;

import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.TransactionException;

public interface FieldService {

	List<Field> queryFieldByFormId(String id);

	int add(Field field) throws TransactionException;

	Field queryFieldById(String id);

	int delete(Form form,Field field ) throws TransactionException;

	int update(Field newField,Form form,Field oldField ) throws TransactionException;

}
