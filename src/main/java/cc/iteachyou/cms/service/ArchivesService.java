package cc.iteachyou.cms.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.ArchivesWithRownum;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.vo.ArchivesVo;

public interface ArchivesService {

	int save(Archives archives,String tableName, Map<String,Object> additional) throws TransactionException;
	
	PageInfo<Map<String, Object>> queryListByPage(SearchEntity params);

	List<Archives> queryListByTagName(String tagName);

	int saveAdditional(String tableName, Map<String, Object> params);

	Map<String, Object> queryArticleById(Map<String, Object> params);

	int update(Archives archives,String tableName, Map<String,Object> additional,String fid) throws TransactionException;

	int updateAdditional(String tableName, Map<String, Object> additional,String fid);

	int delete(String id,Map<String,Object> params) throws TransactionException;

	int deleteAdditional(Map<String, Object> params);

	int updateTagById(Archives article);

	Archives selectByPrimaryKey(String id);

	PageInfo<ArchivesVo> queryListByKeywords(SearchEntity params);

	ArchivesWithRownum queryArticleRowNum(Map<String, Object> params);

	List<Archives> queryListByTop();
	
	int update(Archives archives);

	List<Archives> queryAll(String id);

}
