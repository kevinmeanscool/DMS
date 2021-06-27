package com.system.dms.entity;

public class Mission {
  private Long id;
  private Long mmid;
  private Long missionstatus;
  private java.sql.Timestamp publishtime;
  private java.sql.Timestamp finalupdatetime;
  private Long publisherid;
  private Long receiverid;
  private Long proid;

  public Mission(){}

  public Mission(AssignMission assignMission){
    this.id = assignMission.getId();
    this.mmid = assignMission.getMmid();
    this.missionstatus = assignMission.getMissionstatus();
    this.publishtime = assignMission.getPublishtime();
    this.finalupdatetime = assignMission.getFinalupdatetime();
    this.publisherid = assignMission.getPublisherid();
    this.receiverid = assignMission.getReceiverid();
    this.proid = assignMission.getProid();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMmid() {
    return mmid;
  }

  public void setMmid(Long mmid) {
    this.mmid = mmid;
  }

  public Long getMissionstatus() {
    return missionstatus;
  }

  public void setMissionstatus(Long missionstatus) {
    this.missionstatus = missionstatus;
  }

  public java.sql.Timestamp getPublishtime() {
    return publishtime;
  }

  public void setPublishtime(java.sql.Timestamp publishtime) {
    this.publishtime = publishtime;
  }

  public java.sql.Timestamp getFinalupdatetime() {
    return finalupdatetime;
  }

  public void setFinalupdatetime(java.sql.Timestamp finalupdatetime) {
    this.finalupdatetime = finalupdatetime;
  }

  public Long getPublisherid() {
    return publisherid;
  }

  public void setPublisherid(Long publisherid) {
    this.publisherid = publisherid;
  }

  public Long getReceiverid() {
    return receiverid;
  }

  public void setReceiverid(Long receiverid) {
    this.receiverid = receiverid;
  }

  public Long getProid() {
    return proid;
  }

  public void setProid(Long proid) {
    this.proid = proid;
  }

  @Override
  public String toString() {
    return "Mission{" +
            "id=" + id +
            ", mmid=" + mmid +
            ", missionstatus=" + missionstatus +
            ", publishtime=" + publishtime +
            ", finalupdatetime=" + finalupdatetime +
            ", publisherid=" + publisherid +
            ", receiverid=" + receiverid +
            ", proid=" + proid +
            '}';
  }
}
