package com.system.dms.staticEnum;

public enum AccessRightEnum {
    PRIVATE("私密"),PUBLIC("公开"),PROTECT("保护");

    private String right;

    AccessRightEnum(String right) {
        this.right = right;
    }

    public String getRight() {
        return right;
    }
}
