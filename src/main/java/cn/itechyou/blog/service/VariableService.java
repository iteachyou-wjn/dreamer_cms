package cn.itechyou.blog.service;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Variable;

public interface VariableService {

	PageInfo<Variable> queryListByPage(SearchEntity params);

	void add(Variable variable);

	Variable queryVariableId(String id);

	void updateVariable(Variable variable);

	void deleteVariable(String id);

}
