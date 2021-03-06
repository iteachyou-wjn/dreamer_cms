package cn.itechyou.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.cms.dao.LicenceMapper;
import cn.itechyou.cms.entity.Licence;
import cn.itechyou.cms.service.LicenceService;

@Service
public class LicenceServiceImpl implements LicenceService {

	@Autowired
	private LicenceMapper licenceMapper;
	
	@Override
	public Licence getLicence() {
		return licenceMapper.getCurrentLicence();
	}

	@Override
	public int save(Licence licence) {
		Licence currentLicence = licenceMapper.getCurrentLicence();
		if(currentLicence != null) {
			licenceMapper.deleteByPrimaryKey(currentLicence.getId());
		}
		return licenceMapper.insertSelective(licence);
	}

}
