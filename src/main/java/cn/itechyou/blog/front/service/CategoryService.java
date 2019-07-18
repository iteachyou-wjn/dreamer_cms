package cn.itechyou.blog.front.service;

import java.util.List;

import cn.itechyou.blog.entity.CategoryWithBLOBs;

public interface CategoryService {

	List<CategoryWithBLOBs> navigate(String code);

}
