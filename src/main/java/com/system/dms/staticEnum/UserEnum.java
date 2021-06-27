package com.system.dms.staticEnum;

public enum UserEnum {
    DEFAULTPERSONPROFILE("快写点什么介绍自己吧～"),
    DEFAULTIMAGEURL_MAN("/images/Default_User_man.png"),
    DEFAULTIMAGEURL_WOMAN("/images/Default_User_woman.png"),
    DEFAULTIDENTITY(String.valueOf(UserIdentityEnum.GENERALUSER.ordinal())),
    DEFAULTNICKNAME_PRE("用户"),
    DEFAULTSTATUS(String.valueOf(UserStatusEnum.OFFLINE.ordinal()));

    private String info;

    UserEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }


}
