package cn.itechyou.blog.dao;

import cn.itechyou.blog.entity.System;

public interface SystemMapper {
    int deleteByPrimaryKey(String id);

    int insert(System record);

    int insertSelective(System record);

    System selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(System record);

    int updateByPrimaryKey(System record);

    System selectAll();
}