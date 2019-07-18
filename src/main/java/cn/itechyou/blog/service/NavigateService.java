package cn.itechyou.blog.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Navigate;
import cn.itechyou.blog.entity.NavigatePage;
import cn.itechyou.blog.vo.NavigateVo;

public interface NavigateService {

	PageInfo<Navigate> queryListByPage(SearchEntity params);

	void save(Navigate navigate);

	int update(Navigate navigate);

	Navigate selectById(String id);

	int saveNavPage(NavigatePage[] navigatePages);

	List<NavigateVo> treeList(String navId);

	void updateOrderBy(NavigatePage navigatePage);

	void deletePage(NavigatePage navigatePage);

	int delete(String id);

}
