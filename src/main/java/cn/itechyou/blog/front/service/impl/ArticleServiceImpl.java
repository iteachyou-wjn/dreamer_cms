package cn.itechyou.blog.front.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.dao.ArchivesMapper;
import cn.itechyou.blog.entity.Form;
import cn.itechyou.blog.front.service.ArticleService;
import cn.itechyou.blog.service.CategoryService;
import cn.itechyou.blog.service.FormService;
import cn.itechyou.blog.utils.StringUtil;
import cn.itechyou.blog.vo.ArchivesVo;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArchivesMapper archivesMapper;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FormService formService;
	
	@Override
	public List<Map<String, Object>> list(String ...params) {
		Map<String,Object> entity = new HashMap<String,Object>();
		for(int i = 0; i < params.length;i++) {
			String condition = params[i];
			String key = condition.split("=")[0];
			String value = condition.split("=")[1];
			if("formId".equals(key)) {
				Form form = formService.queryFormByCode(value);
				String tableName = "system_" + form.getTableName();
				entity.put("tableName", tableName);
			}else {
				entity.put(key, value);
			}
		}
		//处理排序
		entity.put("sortWay", StringUtil.isNotBlank(entity.get("sortWay")) ? entity.get("sortWay") : "asc");
		SearchEntity page = new SearchEntity();
		if(entity.get("start") != null && entity.get("length") != null) {
			page.setPageNum(Integer.parseInt(entity.get("start").toString()));
			page.setPageSize(Integer.parseInt(entity.get("length").toString()));
			PageHelper.startPage(page.getPageNum(), page.getPageSize());
		}
		return archivesMapper.queryListByPage(entity);
	}
	
	@Override
	public PageInfo<Map<String, Object>> pagelist(String ...params) {
		Map<String,Object> entity = new HashMap<String,Object>();
		for(int i = 0; i < params.length;i++) {
			String condition = params[i];
			String key = condition.split("=")[0];
			String value = condition.split("=")[1];
			entity.put(key, value);
		}
		//处理排序
		entity.put("sortWay", StringUtil.isNotBlank(entity.get("sortWay")) ? entity.get("sortWay") : "asc");
		SearchEntity page = new SearchEntity();
		if(entity.get("start") != null && entity.get("length") != null) {
			page.setPageNum(Integer.parseInt(entity.get("start").toString()));
			page.setPageSize(Integer.parseInt(entity.get("length").toString()));
		}else {
			page.setPageNum(1);
			page.setPageSize(10);
		}
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<Map<String, Object>> list = archivesMapper.queryListByPage(entity);
		PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(list);
		return pageinfo;
	}

	@Override
	public List<ArchivesVo> recommend(String... params) {
		Map<String,Object> entity = new HashMap<String,Object>();
		for(int i = 0; i < params.length;i++) {
			String condition = params[i];
			String key = condition.split("=")[0];
			String value = condition.split("=")[1];
			entity.put(key, value);
		}
		//处理分页
		SearchEntity page = new SearchEntity();
		if(entity.get("start") != null && entity.get("length") != null) {
			page.setPageNum(Integer.parseInt(entity.get("start").toString()));
			page.setPageSize(Integer.parseInt(entity.get("length").toString()));
		}else {
			page.setPageNum(1);
			page.setPageSize(10);
		}
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<ArchivesVo> list = archivesMapper.queryRecommend(entity);
		return list;
	}
}
