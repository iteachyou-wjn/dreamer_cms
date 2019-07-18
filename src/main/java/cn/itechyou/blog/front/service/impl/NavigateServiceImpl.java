package cn.itechyou.blog.front.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.blog.dao.NavigateMapper;
import cn.itechyou.blog.dao.NavigatePageMapper;
import cn.itechyou.blog.entity.Navigate;
import cn.itechyou.blog.front.service.NavigateService;
import cn.itechyou.blog.vo.NavigateVo;

@Service("navigateService")
public class NavigateServiceImpl implements NavigateService {
	@Autowired
	private NavigateMapper navigateMapper;
	@Autowired
	private NavigatePageMapper navigatePageMapper;
	
	@Override
	public List<NavigateVo> navigate(String code) {
		Navigate nav = navigateMapper.queryNavigateByCode(code);
		if(nav != null) {
			List<NavigateVo> list = getTreeList("-1",nav.getId());
			return list;
		}
		return null;
	}

	private List<NavigateVo> getTreeList(String parentId,String navId) {
		List<NavigateVo> navigateVos = navigatePageMapper.selectByParentId(parentId,navId);
		if(navigateVos != null && navigateVos.size()>0) {
			for(int i= 0;i < navigateVos.size();i++) {
				NavigateVo navigateVo = navigateVos.get(i);
				navigateVo.setNodes(getTreeList(navigateVo.getId(),navId));
			}
		}
		return navigateVos;
	}
}
