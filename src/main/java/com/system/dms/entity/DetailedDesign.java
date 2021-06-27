package com.system.dms.entity;

public class DetailedDesign {
  private Long id;
  private String detname;
  private String processlogic;
  private String arithmetic;
  private String limitingcondition;
  private String input;
  private String output;
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

  public String getDetname() {
    return detname;
  }

  public void setDetname(String detname) {
    this.detname = detname;
  }

  public String getProcesslogic() {
    return processlogic;
  }

  public void setProcesslogic(String processlogic) {
    this.processlogic = processlogic;
  }

  public String getArithmetic() {
    return arithmetic;
  }

  public void setArithmetic(String arithmetic) {
    this.arithmetic = arithmetic;
  }

  public String getLimitingcondition() {
    return limitingcondition;
  }

  public void setLimitingcondition(String limitingcondition) {
    this.limitingcondition = limitingcondition;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
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
    return "DetailedDesign{" +
            "id=" + id +
            ", detname='" + detname + '\'' +
            ", processlogic='" + processlogic + '\'' +
            ", arithmetic='" + arithmetic + '\'' +
            ", limitingcondition='" + limitingcondition + '\'' +
            ", input='" + input + '\'' +
            ", output='" + output + '\'' +
            ", registertime=" + registertime +
            ", finalupdatetime=" + finalupdatetime +
            ", statement=" + statement +
            ", operatorid=" + operatorid +
            '}';
  }
}
