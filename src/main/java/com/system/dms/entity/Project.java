package com.system.dms.entity;

public class Project {
  private Long id;
  private String projectname;
  private String category;
  private String environment;
  private String pattern;
  private Long accessright;
  private java.sql.Timestamp buildtime;
  private java.sql.Timestamp finalupdatetime;
  private Long statement;
  private Long operatorid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProjectname() {
    return projectname;
  }

  public void setProjectname(String projectname) {
    this.projectname = projectname;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public Long getAccessright() {
    return accessright;
  }

  public void setAccessright(Long accessright) {
    this.accessright = accessright;
  }

  public java.sql.Timestamp getBuildtime() {
    return buildtime;
  }

  public void setBuildtime(java.sql.Timestamp buildtime) {
    this.buildtime = buildtime;
  }

  public java.sql.Timestamp getFinalupdatetime() {
    return finalupdatetime;
  }

  public void setFinalupdatetime(java.sql.Timestamp finalupdatetime) {
    this.finalupdatetime = finalupdatetime;
  }

  public Long getStatement() {
    return statement;
  }

  public void setStatement(Long statement) {
    this.statement = statement;
  }

  public Long getOperatorid() {
    return operatorid;
  }

  public void setOperatorid(Long operatorid) {
    this.operatorid = operatorid;
  }

  @Override
  public String toString() {
    return "Project{" +
            "id=" + id +
            ", projectname='" + projectname + '\'' +
            ", category='" + category + '\'' +
            ", environment='" + environment + '\'' +
            ", pattern='" + pattern + '\'' +
            ", accessright=" + accessright +
            ", buildtime=" + buildtime +
            ", finalupdatetime=" + finalupdatetime +
            ", statement=" + statement +
            ", operatorid=" + operatorid +
            '}';
  }
}
