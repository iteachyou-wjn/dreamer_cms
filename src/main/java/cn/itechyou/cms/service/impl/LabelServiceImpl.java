package cn.itechyou.cms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.LabelMapper;
import cn.itechyou.cms.entity.Label;
import cn.itechyou.cms.service.LabelService;
import cn.itechyou.cms.utils.PinyinUtils;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * 标签管理
 * @author CX-kf-001
 *
 */
@Service
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private LabelMapper labelMapper;
	
	public void insertOrUpdate(String[] arr) {
		for(int i = 0;i < arr.length;i++) {
			List<Label> list = labelMapper.selectByLabelName(arr[i]);
			if(list != null && list.size() > 0) {
				Label label = list.get(0);
				int count = Integer.parseInt(label.getTagCount()) + 1;
				label.setTagCount(count + "");
				labelMapper.updateByPrimaryKeySelective(label);
			}else {
				Label label = new Label();
				label.setId(UUIDUtils.getPrimaryKey());
				label.setTagName(arr[i]);
				label.setTagCount(1+"");
				label.setPinyin(PinyinUtils.toPinyin(arr[i]));
				label.setFirstChar(PinyinUtils.toFirstChar(arr[i]).substring(0, 1));
				label.setCreateTime(new Date());
				labelMapper.insertSelective(label);
			}
		}
	}

	@Override
	public void updateCount(String[] arr) {
		for(int i = 0;i < arr.length;i++) {
			List<Label> list = labelMapper.selectByLabelName(arr[i]);
			if(list != null && list.size() > 0) {
				Label label = list.get(0);
				int count = Integer.parseInt(label.getTagCount()) - 1;
				if(count == 0) {
					labelMapper.deleteByPrimaryKey(label.getId());
					continue;
				}
				label.setTagCount(count + "");
				labelMapper.updateByPrimaryKeySelective(label);
			}
		}
	}

	@Override
	public Map<String, List<Map<String,String>>> queryLabel() {
		Map<String,List<Map<String,String>>> map = new LinkedHashMap<String,List<Map<String,String>>>();
		//查询标签组
		List<String> groups = labelMapper.queryGroup();
		if(groups != null && groups.size()>0) {
			for (String group : groups) {
				List<Map<String,String>> list = labelMapper.queryLabelByGroup(group);
				map.put(group.toUpperCase(), list);
			}
		}
		return map;
	}

	@Override
	public int deleteByTagName(String tagName) {
		return labelMapper.deleteByTagName(tagName);
	}

	@Override
	public List<Label> queryAll() {
		return labelMapper.queryAll();
	}
}
