package com.system.dms.entity;

public class MissionModule {
  private Long id;
  private String mimoname;
  private Long category;
  private Long moduleindex;
  private Long statement;
  private java.sql.Timestamp registertime;
  private java.sql.Timestamp finalupdatetime;
  private Long fatherindex;
  private Long operatorid;
  private Long proid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMimoname() {
    return mimoname;
  }

  public void setMimoname(String mimoname) {
    this.mimoname = mimoname;
  }

  public Long getCategory() {
    return category;
  }

  public void setCategory(Long category) {
    this.category = category;
  }

  public Long getModuleindex() {
    return moduleindex;
  }

  public void setModuleindex(Long moduleindex) {
    this.moduleindex = moduleindex;
  }

  public Long getStatement() {
    return statement;
  }

  public void setStatement(Long statement) {
    this.statement = statement;
  }

  public java.sql.Timestamp getRegistertime() {
    return registertime;
  }

  public void setRegistertime(java.sql.Timestamp registertime) {
    this.registertime = registertime;
  }

  public java.sql.Timestamp getFinalupdatetime() {
    return finalupdatetime;
  }

  public void setFinalupdatetime(java.sql.Timestamp finalupdatetime) {
    this.finalupdatetime = finalupdatetime;
  }

  public Long getFatherindex() {
    return fatherindex;
  }

  public void setFatherindex(Long fatherindex) {
    this.fatherindex = fatherindex;
  }

  public Long getOperatorid() {
    return operatorid;
  }

  public void setOperatorid(Long operatorid) {
    this.operatorid = operatorid;
  }

  public Long getProid() {
    return proid;
  }

  public void setProid(Long proid) {
    this.proid = proid;
  }

  @Override
  public String toString() {
    return "MissionModule{" +
            "id=" + id +
            ", mimoname='" + mimoname + '\'' +
            ", category=" + category +
            ", moduleindex=" + moduleindex +
            ", statement=" + statement +
            ", registertime=" + registertime +
            ", finalupdatetime=" + finalupdatetime +
            ", fatherindex=" + fatherindex +
            ", operatorid=" + operatorid +
            ", proid=" + proid +
            '}';
  }
}
