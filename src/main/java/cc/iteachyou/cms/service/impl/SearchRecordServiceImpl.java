package cc.iteachyou.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.iteachyou.cms.dao.SearchRecordMapper;
import cc.iteachyou.cms.entity.SearchRecord;
import cc.iteachyou.cms.service.SearchRecordService;

@Service
public class SearchRecordServiceImpl implements SearchRecordService {
	
	@Autowired
	private SearchRecordMapper searchRecordMapper;

	@Override
	public int add(SearchRecord sr) {
		return searchRecordMapper.insert(sr);
	}

}
