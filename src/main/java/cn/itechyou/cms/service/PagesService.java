package cn.itechyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Pages;
import cn.itechyou.cms.entity.PagesWithBLOBs;

public interface PagesService {

	PageInfo<Pages> queryListByPage(SearchEntity params);

	int save(PagesWithBLOBs page);

	int delete(String id);

	PagesWithBLOBs queryPageById(String id);

	int update(PagesWithBLOBs page);

	List<PagesWithBLOBs> queryAll();

	List<Pages> queryAllShowPages();

	PagesWithBLOBs queryPageByUrl(String path);
}
