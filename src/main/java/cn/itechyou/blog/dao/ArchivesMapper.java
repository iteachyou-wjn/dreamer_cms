package cn.itechyou.blog.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.entity.Archives;
import cn.itechyou.blog.entity.ArchivesWithRownum;
import cn.itechyou.blog.vo.ArchivesVo;

public interface ArchivesMapper {
    int deleteByPrimaryKey(String id);

    int insert(Archives record);

    int insertSelective(Archives record);

    Archives selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Archives record);

    int updateByPrimaryKey(Archives record);
    
    List<ArchivesVo> queryListByPage(Map<String, Object> entity);
    
    List<Archives> queryListByTagName(@Param("tag")String tagName);

	int insertAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> params);

	Map<String, Object> queryArticleById(Map<String, Object> params);

	int updateAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> additional,@Param("id") String fid);

	int deleteAdditional(Map<String, Object> params);

	int updateTagByPrimaryKey(Archives article);

	List<ArchivesVo> queryListByKeywords(@Param("keywords") String keywords);

	ArchivesWithRownum queryArticleRowNum(Map<String, Object> params);

	List<ArchivesVo> queryRecommend(Map<String, Object> entity);
}