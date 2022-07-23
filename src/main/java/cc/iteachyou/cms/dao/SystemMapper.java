package cc.iteachyou.cms.dao;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.System;

public interface SystemMapper extends BaseMapper<System> {
    System getCurrentSystem();
}