package cn.itechyou.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.blog.dao.SearchRecordMapper;
import cn.itechyou.blog.entity.SearchRecord;
import cn.itechyou.blog.service.SearchRecordService;

@Service
public class SearchRecordServiceImpl implements SearchRecordService {
	
	@Autowired
	private SearchRecordMapper searchRecordMapper;

	@Override
	public int add(SearchRecord sr) {
		return searchRecordMapper.insert(sr);
	}

}
