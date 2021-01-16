package cn.itechyou.cms.dao;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.System;

public interface SystemMapper extends BaseMapper<System> {
    System getCurrentSystem();
}