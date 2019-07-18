package cn.itechyou.blog.front.service;

import java.util.List;

import cn.itechyou.blog.vo.NavigateVo;

public interface NavigateService {
	public List<NavigateVo> navigate(String code);
	
}
