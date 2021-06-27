package com.system.dms.staticEnum;

public enum UserIdentityEnum {
    GENERALUSER("普通用户"),MANAGER("管理员"),SUPERMANAGER("超级管理员");

    private String identity;

    UserIdentityEnum(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
