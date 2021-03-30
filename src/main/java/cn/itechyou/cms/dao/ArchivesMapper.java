package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Archives;
import cn.itechyou.cms.entity.ArchivesWithRownum;
import cn.itechyou.cms.vo.ArchivesVo;

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