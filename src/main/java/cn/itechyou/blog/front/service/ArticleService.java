package cn.itechyou.blog.front.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.vo.ArchivesVo;

public interface ArticleService {
	List<ArchivesVo> list(String ...params);
	
	PageInfo<ArchivesVo> pagelist(String ...params);
	
	List<ArchivesVo> recommend(String ...params);
}
