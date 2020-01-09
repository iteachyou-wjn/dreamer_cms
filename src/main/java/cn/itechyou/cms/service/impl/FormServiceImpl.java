package cn.itechyou.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.FormMapper;
import cn.itechyou.cms.entity.Field;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.exception.TransactionException;
import cn.itechyou.cms.service.FieldService;
import cn.itechyou.cms.service.FormService;

@Service
@Transactional(rollbackFor = Exception.class) 
public class FormServiceImpl implements FormService {
	
	@Autowired
	private FormMapper formMapper;
	@Autowired
	private FieldService fieldService;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public PageInfo<Form> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Form> list = formMapper.queryListByPage(params.getEntity());
		PageInfo<Form> page = new PageInfo<Form>(list);
		return page;
	}

	@Override
	public int add(Form form) throws TransactionException {
		int num = 0;
		try {
			num = formMapper.insertSelective(form);
			if(num > 0)
				formMapper.createTable(form);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Override
	public void createTable(Form form) {
		formMapper.createTable(form);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public Form queryFormById(String id) {
		return formMapper.selectByPrimaryKey(id);
	}

	@Override
	public int update(Form newForm, Form oldForm) throws TransactionException {
		int num = 0;
		try {
			num = formMapper.updateByPrimaryKeySelective(newForm);
			Map<String,Object> params = new HashMap<String,Object>();
			if(!oldForm.getTableName().equals("system_"+newForm.getTableName())) {
				params.put("oldTableName", oldForm.getTableName());
				params.put("newTableName", newForm.getTableName());
				formMapper.renameTable(params);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Override
	public void renameTable(Map<String, Object> params) {
		formMapper.renameTable(params);
	}

	@Override
	public int delete(Form form,List<Field> fields) throws TransactionException {
		int num = 0;
		try {
			for(int i = 0;i < fields.size();i++) {
				fieldService.delete(form,fields.get(i));
			}
			num = formMapper.deleteByPrimaryKey(form.getId());
			formMapper.dropTable(form.getTableName());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Override
	public void dropTable(String tableName) {
		formMapper.dropTable(tableName);
	}

	@Override
	public void alterTableAddColumn(String sql) {
		formMapper.alterTableAddColumn(sql);
	}

	@Override
	public void alterTableDropColumn(String sql) {
		formMapper.alterTableDropColumn(sql);
	}

	@Override
	public void alterTableChangeColumn(String sql) {
		formMapper.alterTableChangeColumn(sql);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public List<Form> queryAll() {
		return formMapper.queryAll();
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public Form queryDefaultForm() {
		return formMapper.queryDefaultForm();
	}

	@Override
	public Form queryFormByCode(String value) {
		return formMapper.queryFormByCode(value);
	}

}
