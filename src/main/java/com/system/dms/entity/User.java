package com.system.dms.entity;

public class User {
  private Long id;
  private String username;
  private String password;
  private String email;
  private String gender;
  private String personprofile;
  private String imageurl;
  private String identity;
  private String nickname;
  private Long status;
  private java.sql.Timestamp registertime;
  private java.sql.Timestamp finalupdatetime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPersonprofile() {
    return personprofile;
  }

  public void setPersonprofile(String personprofile) {
    this.personprofile = personprofile;
  }

  public String getImageurl() {
    return imageurl;
  }

  public void setImageurl(String imageurl) {
    this.imageurl = imageurl;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
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

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", gender='" + gender + '\'' +
            ", personprofile='" + personprofile + '\'' +
            ", imageurl='" + imageurl + '\'' +
            ", identity='" + identity + '\'' +
            ", nickname='" + nickname + '\'' +
            ", status=" + status +
            ", registertime=" + registertime +
            ", finalupdatetime=" + finalupdatetime +
            '}';
  }
}
