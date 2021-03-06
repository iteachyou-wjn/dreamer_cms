package cn.itechyou.cms.service;

import cn.itechyou.cms.entity.Licence;

public interface LicenceService {

	Licence getLicence();

	int save(Licence licence);

}
