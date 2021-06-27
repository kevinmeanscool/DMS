package com.system.dms.entity;

public class TotalDesign {
  private Long id;
  private String tolname;
  private String processlogic;
  private String softwarestructure;
  private java.sql.Timestamp registertime;
  private java.sql.Timestamp finalupdatetime;
  private Long statement;
  private Long operatorid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTolname() {
    return tolname;
  }

  public void setTolname(String tolname) {
    this.tolname = tolname;
  }

  public String getProcesslogic() {
    return processlogic;
  }

  public void setProcesslogic(String processlogic) {
    this.processlogic = processlogic;
  }

  public String getSoftwarestructure() {
    return softwarestructure;
  }

  public void setSoftwarestructure(String softwarestructure) {
    this.softwarestructure = softwarestructure;
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
}
