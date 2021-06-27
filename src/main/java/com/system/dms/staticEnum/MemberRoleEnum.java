package com.system.dms.staticEnum;

public enum MemberRoleEnum {
    Independent("独立开发者"),ProjectBuilder("项目发起者"),ProductManager("产品经理"),ProjectManager("项目经理"),GroupLeader("项目组组长"),Programmer("软件开发工程师"),QA("软件测试工程师");

    private String role;

    MemberRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
