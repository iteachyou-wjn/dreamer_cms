package cn.itechyou.blog.front.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.blog.dao.PagesMapper;
import cn.itechyou.blog.entity.PagesWithBLOBs;
import cn.itechyou.blog.front.service.PageService;

@Service("pageService")
public class PageServiceImpl implements PageService {
	@Autowired
	private PagesMapper pagesMapper;

	@Override
	public PagesWithBLOBs pageinfo(String code) {
		return pagesMapper.queryPageByCode(code);
	}

}
