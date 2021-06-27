package com.system.dms.staticEnum;

public enum MissionStatusEnum {
    Unfinished("未完成"),finished("完成"),edited("已编码"),testing("待测试"),repairing("待修复");

    private String status;

    MissionStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
