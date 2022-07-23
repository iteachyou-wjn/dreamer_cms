package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.ArchivesWithRownum;
import cc.iteachyou.cms.vo.ArchivesVo;

public interface ArchivesMapper extends BaseMapper<Archives> {
    
    List<Map<String,Object>> queryListByPage(Map<String, Object> entity);
    
    List<Archives> queryListByTagName(@Param("tag")String tagName);

	int insertAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> params);

	Map<String, Object> queryArticleById(Map<String, Object> params);

	int updateAdditional(@Param("tableName") String tableName, @Param("cols") Map<String, Object> additional,@Param("id") String fid);

	int deleteAdditional(Map<String, Object> params);

	int updateTagByPrimaryKey(Archives article);

	List<ArchivesVo> queryListByKeywords(Map<String, Object> entity);

	ArchivesWithRownum queryArticleRowNum(Map<String, Object> params);

	List<ArchivesVo> queryRecommend(Map<String, Object> entity);

	List<Archives> queryListByTop();

	List<Archives> queryAll(@Param("categoryId") String id);
}