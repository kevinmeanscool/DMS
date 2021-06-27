package com.system.dms.service;

import com.system.dms.entity.Securitycode;

public interface SecurityCodeService {
    boolean sendSecurityCodeToEmail(String email);
    boolean addRecordOfSecurityCode(Securitycode securitycode);
    boolean isEmailUsed(Securitycode securitycode);
    Securitycode getSecuritycodeObjectByUsername(String username);
    boolean isUserNameExisting(String username);
    boolean isEmailExisting(String email);
}
