package com.system.dms.entity;

import java.util.List;

public class TreeGraph {

    private long missionStat;
    private long category;
    private List<TreeGraph> children;
    private String parent;
    private String name;
    private long id;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<TreeGraph> getChildren() {
        return children;
    }

    public void setChildren(List<TreeGraph> children) {
        this.children = children;
    }

    public long getMissionStat() {
        return missionStat;
    }

    public void setMissionStat(long missionStat) {
        this.missionStat = missionStat;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TreeGraph{" +
                "missionStat=" + missionStat +
                ", category=" + category +
                ", children=" + children +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
