package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.ArchivesWithRownum;
import cn.itechyou.cms.vo.ArchivesVo;

public interface ArchivesMapper {
    int deleteByPrimaryKey(String id);

    int insert(Archives record);

    int insertSelective(Archives record);

    Archives selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Archives record);

    int updateByPrimaryKey(Archives record);
    
    List<Map<String,Object>> queryListByPage(Map<String, Object> entity);
    
    List<Archives> queryListByTagName(@Param("tag")String tagName);

	int insertAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> params);

	Map<String, Object> queryArticleById(Map<String, Object> params);

	int updateAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> additional,@Param("id") String fid);

	int deleteAdditional(Map<String, Object> params);

	int updateTagByPrimaryKey(Archives article);

	List<ArchivesVo> queryListByKeywords(@Param("keywords") String keywords);

	ArchivesWithRownum queryArticleRowNum(Map<String, Object> params);

	List<ArchivesVo> queryRecommend(Map<String, Object> entity);

	List<Archives> queryListByTop();

	List<Archives> queryAll(@Param("categoryId") String id);
}