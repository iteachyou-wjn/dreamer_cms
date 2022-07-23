package cc.iteachyou.cms.service;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Scheduled;

public interface ScheduledService {

	PageInfo<Scheduled> queryListByPage(SearchEntity params);

	int add(Scheduled scheduled);
	
	int update(Scheduled scheduled);

	Scheduled queryScheduledById(String id);

	int delete(String id);

}
