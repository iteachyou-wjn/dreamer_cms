package cn.itechyou.blog.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import cn.itechyou.blog.dao.SystemMapper;
import cn.itechyou.blog.entity.System;
import cn.itechyou.blog.service.SystemService;
import cn.itechyou.blog.utils.FileConfiguration;
import cn.itechyou.blog.utils.LoggerUtils;

/**
 * 系统设置业务处理类
 * 
 * @author LIGW
 */
@Service
public class SystemServiceImpl implements SystemService {

	/**
	 * 日志输出
	 */
	private static final Logger logger = LoggerUtils.getLogger(SystemServiceImpl.class);
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private FileConfiguration fileConfiguration;

	/**
	 * 系统设置列表
	 */
	@Override
	public System getSystemList() {
		System System = systemMapper.selectAll();
		return System;
	}

	/**
	 * 更新系统设置
	 */
	@Override
	public int update(System system) {
		int num = 0;
		ObjectOutputStream oos = null; 
		OutputStream out = null;
		InputStream in = null;
		try {
			num = systemMapper.updateByPrimaryKey(system);
			String rootPath = fileConfiguration.getUploadDir();
			File dir = new File(rootPath);
			if(dir == null || !dir.exists()) {
				dir.mkdirs();
			}
			String path = rootPath + File.separator + "system.properties";
			File file = new File(path);
			if (null == file || !file.exists()) {
				file.createNewFile();
			}
			in = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(in);
			properties.clear(); //清空Properties文件
			properties.setProperty("id", system.getId());
			properties.setProperty("website", system.getWebsite());
			properties.setProperty("title", system.getTitle());
			properties.setProperty("keywords", system.getKeywords());
			properties.setProperty("describe", system.getDescribe());
			properties.setProperty("icp", system.getIcp());
			properties.setProperty("copyright", system.getCopyright());
			properties.setProperty("uploaddir", system.getUploaddir());
			properties.setProperty("appid", system.getAppid());
			properties.setProperty("appkey", system.getAppkey());
			out = new FileOutputStream(file);
			properties.store(out, "update system_setting");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return num;
	}

}
