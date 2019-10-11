package cn.itechyou.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.cms.dao.SearchRecordMapper;
import cn.itechyou.cms.entity.SearchRecord;
import cn.itechyou.cms.service.SearchRecordService;

@Service
public class SearchRecordServiceImpl implements SearchRecordService {
	
	@Autowired
	private SearchRecordMapper searchRecordMapper;

	@Override
	public int add(SearchRecord sr) {
		return searchRecordMapper.insert(sr);
	}

}
