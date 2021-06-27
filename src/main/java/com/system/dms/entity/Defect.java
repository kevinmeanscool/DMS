package com.system.dms.entity;

public class Defect {
  private Long id;
  private String title;
  private String defectdescription;
  private Long severity;
  private java.sql.Timestamp submissiontime;
  private String solutiondescription;
  private String note;
  private Long operatorid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDefectdescription() {
    return defectdescription;
  }

  public void setDefectdescription(String defectdescription) {
    this.defectdescription = defectdescription;
  }

  public Long getSeverity() {
    return severity;
  }

  public void setSeverity(Long severity) {
    this.severity = severity;
  }

  public java.sql.Timestamp getSubmissiontime() {
    return submissiontime;
  }

  public void setSubmissiontime(java.sql.Timestamp submissiontime) {
    this.submissiontime = submissiontime;
  }

  public String getSolutiondescription() {
    return solutiondescription;
  }

  public void setSolutiondescription(String solutiondescription) {
    this.solutiondescription = solutiondescription;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getOperatorid() {
    return operatorid;
  }

  public void setOperatorid(Long operatorid) {
    this.operatorid = operatorid;
  }

  @Override
  public String toString() {
    return "Defect{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", defectdescription='" + defectdescription + '\'' +
            ", severity=" + severity +
            ", submissiontime=" + submissiontime +
            ", solutiondescription='" + solutiondescription + '\'' +
            ", note='" + note + '\'' +
            ", operatorid=" + operatorid +
            '}';
  }
}
