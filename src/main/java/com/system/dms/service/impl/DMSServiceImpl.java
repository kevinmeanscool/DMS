package com.system.dms.service.impl;

import com.system.dms.dao.DMSMapper;
import com.system.dms.entity.DMS;
import com.system.dms.service.DMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DMSService")
public class DMSServiceImpl implements DMSService{
    @Autowired
    private DMSMapper dmsMapper;

    @Override
    public DMS getBasicInfoOfSystem() {
        return dmsMapper.getBasicInfoOfSystem();
    }

    @Override
    public boolean updateBasicInfoOfSystem(DMS DMS) {
        return dmsMapper.updateBasicInfoOfSystem(DMS);
    }
}
