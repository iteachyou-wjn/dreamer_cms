package cn.itechyou.cms.dao;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Licence;

public interface LicenceMapper extends BaseMapper<Licence> {

	Licence getCurrentLicence();
    
}