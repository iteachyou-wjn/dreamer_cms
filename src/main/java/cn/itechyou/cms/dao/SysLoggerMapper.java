package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.cms.entity.SysLogger;

public interface SysLoggerMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLogger record);

    int insertSelective(SysLogger record);

    SysLogger selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLogger record);

    int updateByPrimaryKeyWithBLOBs(SysLogger record);

    int updateByPrimaryKey(SysLogger record);

	List<SysLogger> queryListByPage(Map<String, Object> entity);
}