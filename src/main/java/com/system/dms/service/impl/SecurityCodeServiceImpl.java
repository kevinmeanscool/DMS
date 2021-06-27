package com.system.dms.service.impl;

import com.system.dms.dao.SecurityCodeMapper;
import com.system.dms.entity.Securitycode;
import com.system.dms.service.SecurityCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SecurityCodeService")
public class SecurityCodeServiceImpl implements SecurityCodeService{

    @Autowired
    private SecurityCodeMapper securityCodeMapper;

    @Override
    public boolean sendSecurityCodeToEmail(String email) {
        return false;
    }

    @Override
    public boolean addRecordOfSecurityCode(Securitycode securitycode) {
        if(securityCodeMapper.isRecordExisting(securitycode)!=0){
            return securityCodeMapper.updateRecordOfSecurityCode(securitycode);
        }
        else {
            return securityCodeMapper.addRecordOfSecurityCode(securitycode);
        }
    }

    @Override
    public boolean isEmailUsed(Securitycode securitycode) {
        if(securityCodeMapper.isEmailUsed(securitycode)==0)
            return false;
        else
            return true;
    }

    @Override
    public Securitycode getSecuritycodeObjectByUsername(String username) {
        return securityCodeMapper.getSecuritycodeObjectByUsername(username);
    }

    @Override
    public boolean isUserNameExisting(String username) {
        if(securityCodeMapper.isUserNameExisting(username)==1)
            return true;
        else
            return false;
    }

    @Override
    public boolean isEmailExisting(String email) {
        if(securityCodeMapper.isEmailExisting(email)==0){
            return false;
        }
        else {
            return true;
        }
    }
}
