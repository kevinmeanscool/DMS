package com.system.dms.staticEnum;

public enum DMSStatusEnum {
    RUNNING("运行"),MAINTAIN("维护");

    private String status;

    DMSStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
