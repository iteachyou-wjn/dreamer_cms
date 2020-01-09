package cn.itechyou.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cn.itechyou.cms.dao.FieldMapper;
import cn.itechyou.cms.dao.FormMapper;
import cn.itechyou.cms.entity.Field;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.exception.TransactionException;
import cn.itechyou.cms.service.FieldService;

@Service
@Transactional(rollbackFor = Exception.class) 
public class FieldServiceImpl implements FieldService {
	
	@Autowired
	private FieldMapper fieldMapper;
	@Autowired
	private FormMapper formMapper;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public List<Field> queryFieldByFormId(String id) {
		return fieldMapper.queryFieldByFormId(id);
	}

	@Override
	public int add(Field field) throws TransactionException {
		int num = 0;
		try {
			num = fieldMapper.insertSelective(field);
			if(num > 0) {
				//查询表单
				Form form = formMapper.selectByPrimaryKey(field.getFormId());
				
				StringBuffer sql = new StringBuffer();
				sql.append("alter table `system_" + form.getTableName() + "` ");
				if("varchar".equals(field.getDataType()) ||
					"char".equals(field.getDataType()) ||
					"file".equals(field.getDataType()) ||
					"image".equals(field.getDataType())) {
					sql.append("add `" + field.getFieldName() + "` varchar(" + field.getMaxLength() + ") default '" + field.getDefaultValue() + "'");
				}else if("textarea".equals(field.getDataType()) ||
						"html".equals(field.getDataType()) ||
						"markdown".equals(field.getDataType()) ||
						"imageset".equals(field.getDataType())) {
					sql.append("add `" + field.getFieldName() + "` mediumtext");
				}else if("datetime".equals(field.getDataType())) {
					sql.append("add `" + field.getFieldName() + "` datetime");
				}else if("radio".equals(field.getDataType()) ||
						"checkbox".equals(field.getDataType()) ||
						"select".equals(field.getDataType())) {
					sql.append("add `" + field.getFieldName() + "` varchar(" + field.getMaxLength() + ") default '" + field.getDefaultValue() + "'");
				}
				formMapper.alterTableAddColumn(sql.toString());
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public Field queryFieldById(String id) {
		return fieldMapper.selectByPrimaryKey(id);
	}

	@Override
	public int delete(Form form, Field field ) throws TransactionException {
		
		int num = 0;
		try {
			num = fieldMapper.deleteByPrimaryKey(field.getId());
			StringBuffer sql = new StringBuffer();
			sql.append("alter table `system_" + form.getTableName() + "` drop column `" + field.getFieldName() + "`");
			formMapper.alterTableDropColumn(sql.toString());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Override
	public int update(Field newField, Form form, Field oldField ) throws TransactionException {
		
		int num = 0;
		try {
			num = fieldMapper.updateByPrimaryKeySelective(newField);
			
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sql = new StringBuffer();
			sql.append("alter table `system_" + form.getTableName() + "` ");
			if("varchar".equals(newField.getDataType()) ||
				"char".equals(newField.getDataType()) ||
				"file".equals(newField.getDataType()) ||
				"image".equals(newField.getDataType())) {
				sql.append("change `" + oldField.getFieldName() + "` `" + newField.getFieldName() + "` varchar(" + newField.getMaxLength() + ") default '" + newField.getDefaultValue() + "'");
			}else if("textarea".equals(newField.getDataType()) ||
					"html".equals(newField.getDataType()) ||
					"markdown".equals(newField.getDataType()) ||
					"imageset".equals(newField.getDataType())) {
				sql.append("change `" + oldField.getFieldName() + "` `" + newField.getFieldName() + "` text");
			}else if("datetime".equals(newField.getDataType())) {
				sql.append("change `" + oldField.getFieldName() + "` `" + newField.getFieldName() + "` datetime");
			}else if("radio".equals(newField.getDataType()) ||
					"checkbox".equals(newField.getDataType()) ||
					"select".equals(newField.getDataType())) {
				sql.append("change `" + oldField.getFieldName() + "` `" + newField.getFieldName() + "` varchar(" + newField.getMaxLength() + ") default '" + newField.getDefaultValue() + "'");
			}
			formMapper.alterTableChangeColumn(sql.toString());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

}
