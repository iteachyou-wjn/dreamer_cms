package cn.itechyou.blog.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Archives;
import cn.itechyou.blog.entity.ArchivesWithRownum;
import cn.itechyou.blog.exception.TransactionException;
import cn.itechyou.blog.vo.ArchivesVo;

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

}
