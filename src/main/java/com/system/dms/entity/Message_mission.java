package com.system.dms.entity;

public class Message_mission {
  private Long id;
  private Long subject_id;
  private Long object_id;
  private String message_mission;
  private java.sql.Timestamp publishtime;
  private Long proid;
  private String viewflag;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSubject_id() {
    return subject_id;
  }

  public void setSubject_id(Long subject_id) {
    this.subject_id = subject_id;
  }

  public Long getObject_id() {
    return object_id;
  }

  public void setObject_id(Long object_id) {
    this.object_id = object_id;
  }

  public String getMessage_mission() {
    return message_mission;
  }

  public void setMessage_mission(String message_mission) {
    this.message_mission = message_mission;
  }

  public java.sql.Timestamp getPublishtime() {
    return publishtime;
  }

  public void setPublishtime(java.sql.Timestamp publishtime) {
    this.publishtime = publishtime;
  }

  public Long getProid() {
    return proid;
  }

  public void setProid(Long proid) {
    this.proid = proid;
  }

  public String getViewflag() {
    return viewflag;
  }

  public void setViewflag(String viewflag) {
    this.viewflag = viewflag;
  }
}
