package cn.itechyou.blog.service;

import java.util.List;

import cn.itechyou.blog.entity.System;

public interface SystemService {

	System getSystemList();

	int update(System system);

}
