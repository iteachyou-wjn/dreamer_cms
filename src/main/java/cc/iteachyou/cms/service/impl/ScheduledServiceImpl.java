package cc.iteachyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ScheduledMapper;
import cc.iteachyou.cms.entity.Scheduled;
import cc.iteachyou.cms.service.ScheduledService;

@Service
public class ScheduledServiceImpl implements ScheduledService {

	@Autowired
	private ScheduledMapper scheduledMapper;
	
	@Override
	public PageInfo<Scheduled> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Scheduled> list = scheduledMapper.queryListByPage(params.getEntity());
		PageInfo<Scheduled> page = new PageInfo<Scheduled>(list);
		return page;
	}

	@Override
	public int add(Scheduled scheduled) {
		return scheduledMapper.insertSelective(scheduled);
	}
	
	@Override
	public int update(Scheduled scheduled) {
		return scheduledMapper.updateByPrimaryKeySelective(scheduled);
	}

	@Override
	public Scheduled queryScheduledById(String id) {
		return scheduledMapper.selectByPrimaryKey(id);
	}

	@Override
	public int delete(String id) {
		return scheduledMapper.deleteByPrimaryKey(id);
	}

}
