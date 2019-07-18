package cn.itechyou.blog.service;

import java.util.List;

import cn.itechyou.blog.entity.Field;
import cn.itechyou.blog.entity.Form;
import cn.itechyou.blog.exception.TransactionException;

public interface FieldService {

	List<Field> queryFieldByFormId(String id);

	int add(Field field) throws TransactionException;

	Field queryFieldById(String id);

	int delete(Form form,Field field ) throws TransactionException;

	int update(Field newField,Form form,Field oldField ) throws TransactionException;

}
