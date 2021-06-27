package com.system.dms.staticEnum;

public enum  MissionMuduleEnum {
    Project("项目"),Requirement("需求"),TotalDesign("总体设计"),DetailedDesign("详细设计"),Defect("缺陷");

    private String moduleType;

    private MissionMuduleEnum(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleType() {
        return moduleType;
    }
}
