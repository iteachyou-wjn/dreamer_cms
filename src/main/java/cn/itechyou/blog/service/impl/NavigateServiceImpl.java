package cn.itechyou.blog.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.dao.NavigateMapper;
import cn.itechyou.blog.dao.NavigatePageMapper;
import cn.itechyou.blog.entity.Navigate;
import cn.itechyou.blog.entity.NavigatePage;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.service.NavigateService;
import cn.itechyou.blog.utils.UUIDUtils;
import cn.itechyou.blog.vo.NavigateVo;

@Service
public class NavigateServiceImpl implements NavigateService {

	@Autowired
	private NavigateMapper navigateMapper;
	@Autowired
	private NavigatePageMapper navigatePageMapper;
	
	@Override
	public PageInfo<Navigate> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Navigate> list = navigateMapper.queryListByPage(params.getEntity());
		PageInfo<Navigate> page = new PageInfo<Navigate>(list);
		return page;	
	}

	@Override
	public void save(Navigate navigate) {
		navigateMapper.insertSelective(navigate);
	}

	@Override
	public int update(Navigate navigate) {
		return navigateMapper.updateByPrimaryKeySelective(navigate);
	}

	@Override
	public Navigate selectById(String id) {
		return navigateMapper.selectByPrimaryKey(id);
	}

	@Override
	public int saveNavPage(NavigatePage[] navigatePages) {
		
		int num = 0;
		for(int i = 0 ;i<navigatePages.length;i++) {
			NavigatePage navigatePage = navigatePages[i];
			navigatePage.setId(UUIDUtils.getPrimaryKey());
			navigatePage.setCreateBy(TokenManager.getToken().getId());
			navigatePage.setCreateTime(new Date());
			int count = navigatePageMapper.insertSelective(navigatePage);
			num += count;
		}
		return num;
	}

	@Override
	public List<NavigateVo> treeList(String navId) {
		List<NavigateVo> list = getTreeList("-1",navId);
		return list;
	}

	public List<NavigateVo> getTreeList(String parentId,String navId) {
		List<NavigateVo> navigateVos = navigatePageMapper.selectByParentId(parentId,navId);
		if(navigateVos != null && navigateVos.size()>0) {
			for(int i= 0;i < navigateVos.size();i++) {
				NavigateVo navigateVo = navigateVos.get(i);
				navigateVo.setNodes(getTreeList(navigateVo.getId(),navId));
			}
		}
		return navigateVos;
	}

	@Override
	public void updateOrderBy(NavigatePage navigatePage) {
		
		int num = navigatePageMapper.updateByPrimaryKeySelective(navigatePage);
	}

	@Override
	public void deletePage(NavigatePage navigatePage) {
		int num = navigatePageMapper.deleteByPrimaryKey(navigatePage.getId());
	}

	@Override
	public int delete(String id) {
		int row = navigatePageMapper.deletePageByNavId(id);
		row = navigateMapper.deleteByPrimaryKey(id);
		return row;
	}
}
