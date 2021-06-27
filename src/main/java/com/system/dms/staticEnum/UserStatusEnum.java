package com.system.dms.staticEnum;

public enum UserStatusEnum {
    ONLINE("在线"),OFFLINE("离线");

    private String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
