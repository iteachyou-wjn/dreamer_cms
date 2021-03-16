package cn.itechyou.cms.service;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Scheduled;

public interface ScheduledService {

	PageInfo<Scheduled> queryListByPage(SearchEntity params);

	int add(Scheduled scheduled);
	
	int update(Scheduled scheduled);

	Scheduled queryScheduledById(String id);

	int delete(String id);

}
