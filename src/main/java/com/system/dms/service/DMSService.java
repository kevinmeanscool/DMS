package com.system.dms.service;

import com.system.dms.entity.DMS;

public interface DMSService {
    /**
     * 获取系统基本信息
     * @return 系统对象
     */
    DMS getBasicInfoOfSystem();

    /**
     * 更新系统基本信息
     * @param DMS
     * @return bool结果
     */
    boolean updateBasicInfoOfSystem(DMS DMS);
}
