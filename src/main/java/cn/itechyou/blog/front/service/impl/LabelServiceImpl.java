package cn.itechyou.blog.front.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.blog.dao.LabelMapper;
import cn.itechyou.blog.entity.Label;
import cn.itechyou.blog.front.service.LabelService;

@Service("labelService")
public class LabelServiceImpl implements LabelService {
	@Autowired
	private LabelMapper labelMapper;
	
	@Override
	public List<Label> all() {
		return labelMapper.queryAll();
	}
}
