package cc.iteachyou.cms.service;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Variable;

public interface VariableService {

	PageInfo<Variable> queryListByPage(SearchEntity params);

	void add(Variable variable);

	Variable queryVariableId(String id);

	void updateVariable(Variable variable);

	void deleteVariable(String id);

	Variable queryVariableByName(String name);

}
