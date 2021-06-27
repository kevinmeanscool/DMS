package com.system.dms.entity;

public class ProjectMember {
  private Long id;
  private Long proid;
  private Long userid;
  private String membername;
  private Long roletype;
  private java.sql.Timestamp registertime;
  private java.sql.Timestamp finalupdatetime;
  private Long statement;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProid() {
    return proid;
  }

  public void setProid(Long proid) {
    this.proid = proid;
  }

  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  public String getMembername() {
    return membername;
  }

  public void setMembername(String membername) {
    this.membername = membername;
  }

  public Long getRoletype() {
    return roletype;
  }

  public void setRoletype(Long roletype) {
    this.roletype = roletype;
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

  @Override
  public String toString() {
    return "ProjectMember{" +
            "id=" + id +
            ", proid=" + proid +
            ", userid=" + userid +
            ", membername='" + membername + '\'' +
            ", roletype=" + roletype +
            ", registertime=" + registertime +
            ", finalupdatetime=" + finalupdatetime +
            ", statement=" + statement +
            '}';
  }
}
