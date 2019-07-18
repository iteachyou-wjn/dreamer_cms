package cn.itechyou.blog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.blog.entity.NavigatePage;
import cn.itechyou.blog.vo.NavigateVo;

public interface NavigatePageMapper {
    int deleteByPrimaryKey(String id);

    int insert(NavigatePage record);

    int insertSelective(NavigatePage record);

    NavigatePage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NavigatePage record);

    int updateByPrimaryKey(NavigatePage record);

	List<NavigateVo> selectByParentId(String parentId,String navId);

	int deletePageByNavId(@Param("navId") String id);
}