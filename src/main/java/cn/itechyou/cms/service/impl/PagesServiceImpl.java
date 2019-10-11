package cn.itechyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.PagesMapper;
import cn.itechyou.cms.entity.Pages;
import cn.itechyou.cms.entity.PagesWithBLOBs;
import cn.itechyou.cms.service.PagesService;

/**
 * 页面管理
 * 
 * @author 王俊南
 *
 */
@Service
public class PagesServiceImpl implements PagesService {
	@Autowired
	private PagesMapper pagesMapper;

	@Override
	public PageInfo<Pages> queryListByPage(SearchEntity params) {
		if (params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if (params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Pages> list = pagesMapper.queryListByPage(params.getEntity());
		PageInfo<Pages> page = new PageInfo<Pages>(list);
		return page;
	}

	@Override
	public int save(PagesWithBLOBs page) {
		return this.pagesMapper.insertSelective(page);
	}

	@Override
	public int delete(String id) {
		return this.pagesMapper.deleteByPrimaryKey(id);
	}

	@Override
	public PagesWithBLOBs queryPageById(String id) {
		return this.pagesMapper.selectByPrimaryKey(id);
	}

	@Override
	public int update(PagesWithBLOBs page) {
		return this.pagesMapper.updateByPrimaryKeySelective(page);
	}

	@Override
	public List<PagesWithBLOBs> queryAll() {
		return this.pagesMapper.queryAll();
	}

	@Override
	public List<Pages> queryAllShowPages() {
		return this.pagesMapper.queryAllShowPages();
	}

	@Override
	public PagesWithBLOBs queryPageByUrl(String path) {
		return this.pagesMapper.queryPageByUrl(path);
	}
}
