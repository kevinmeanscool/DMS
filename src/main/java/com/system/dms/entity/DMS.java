package com.system.dms.entity;

public class DMS {
  private Long id;
  private String systemname;
  private String copyright;
  private Long status;
  private java.sql.Timestamp foundingtime;
  private java.sql.Timestamp finalupdatetime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSystemname() {
    return systemname;
  }

  public void setSystemname(String systemname) {
    this.systemname = systemname;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public java.sql.Timestamp getFoundingtime() {
    return foundingtime;
  }

  public void setFoundingtime(java.sql.Timestamp foundingtime) {
    this.foundingtime = foundingtime;
  }

  public java.sql.Timestamp getFinalupdatetime() {
    return finalupdatetime;
  }

  public void setFinalupdatetime(java.sql.Timestamp finalupdatetime) {
    this.finalupdatetime = finalupdatetime;
  }

  @Override
  public String toString() {
    return "DMS{" +
            "id=" + id +
            ", systemname='" + systemname + '\'' +
            ", copyright='" + copyright + '\'' +
            ", status=" + status +
            ", foundingtime=" + foundingtime +
            ", finalupdatetime=" + finalupdatetime +
            '}';
  }
}
