package cn.itechyou.cms.dao;

import cn.itechyou.cms.entity.System;

public interface SystemMapper {
    int deleteByPrimaryKey(String id);

    int insert(System record);

    int insertSelective(System record);

    System selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(System record);

    int updateByPrimaryKey(System record);

    System selectAll();
}