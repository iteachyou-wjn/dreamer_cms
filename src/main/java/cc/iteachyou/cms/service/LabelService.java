package cc.iteachyou.cms.service;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.entity.Label;

public interface LabelService {
	public void insertOrUpdate(String[] arr);

	public void updateCount(String[] tagArr);

	public Map<String, List<Map<String,String>>> queryLabel();

	public int deleteByTagName(String tagName);
	
	public List<Label> queryAll();

}
