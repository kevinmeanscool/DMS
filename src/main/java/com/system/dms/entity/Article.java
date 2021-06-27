package com.system.dms.entity;

public class Article {
  private Long id;
  private String title;
  private String content;
  private java.sql.Timestamp publishtime;
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public java.sql.Timestamp getPublishtime() {
    return publishtime;
  }

  public void setPublishtime(java.sql.Timestamp publishtime) {
    this.publishtime = publishtime;
  }

  public Long getOperatorid() {
    return operatorid;
  }

  public void setOperatorid(Long operatorid) {
    this.operatorid = operatorid;
  }

  @Override
  public String toString() {
    return "Article{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", publishtime=" + publishtime +
            ", operatorid=" + operatorid +
            '}';
  }
}
