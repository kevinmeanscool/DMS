package com.system.dms.entity;

public class Requirement {
  private Long id;
  private String reqname;
  private String businessreq;
  private String userreq;
  private String functionalreq;
  private String nonfunctionalreq;
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

  public String getReqname() {
    return reqname;
  }

  public void setReqname(String reqname) {
    this.reqname = reqname;
  }

  public String getBusinessreq() {
    return businessreq;
  }

  public void setBusinessreq(String businessreq) {
    this.businessreq = businessreq;
  }

  public String getUserreq() {
    return userreq;
  }

  public void setUserreq(String userreq) {
    this.userreq = userreq;
  }

  public String getFunctionalreq() {
    return functionalreq;
  }

  public void setFunctionalreq(String functionalreq) {
    this.functionalreq = functionalreq;
  }

  public String getNonfunctionalreq() {
    return nonfunctionalreq;
  }

  public void setNonfunctionalreq(String nonfunctionalreq) {
    this.nonfunctionalreq = nonfunctionalreq;
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

  @Override
  public String toString() {
    return "Requirement{" +
            "id=" + id +
            ", reqname='" + reqname + '\'' +
            ", businessreq='" + businessreq + '\'' +
            ", userreq='" + userreq + '\'' +
            ", functionalreq='" + functionalreq + '\'' +
            ", nonfunctionalreq='" + nonfunctionalreq + '\'' +
            ", registertime=" + registertime +
            ", finalupdatetime=" + finalupdatetime +
            ", statement=" + statement +
            ", operatorid=" + operatorid +
            '}';
  }
}
