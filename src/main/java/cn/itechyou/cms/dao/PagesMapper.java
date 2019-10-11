package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Pages;
import cn.itechyou.cms.entity.PagesWithBLOBs;

public interface PagesMapper {
    int deleteByPrimaryKey(String id);

    int insert(PagesWithBLOBs record);

    int insertSelective(PagesWithBLOBs record);

    PagesWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PagesWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PagesWithBLOBs record);

    int updateByPrimaryKey(Pages record);

	List<Pages> queryListByPage(Map<String, Object> entity);

	List<PagesWithBLOBs> queryAll();
	
	List<Pages> queryAllShowPages();

	PagesWithBLOBs queryPageByUrl(@Param("path") String path);
	
	PagesWithBLOBs queryPageByCode(@Param("code") String code);
}