package cn.itechyou.blog.front.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.vo.ArchivesVo;

public interface ArticleService {
	List<Map<String, Object>> list(String ...params);
	
	PageInfo<Map<String, Object>> pagelist(String ...params);
	
	List<ArchivesVo> recommend(String ...params);
}
