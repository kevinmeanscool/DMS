package com.system.dms.staticEnum;

public enum StatementEnum {
    Initial("初始"),Modified("已编辑"),Deleted("已删除"),UnPublished("未发布"),Published("已发布");

    private String statement;

    StatementEnum(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }
}
