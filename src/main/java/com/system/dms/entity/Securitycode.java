package com.system.dms.entity;

public class Securitycode {
  private Long id;
  private String username;
  private Long securitycode;
  private String email;

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

  public Long getSecuritycode() {
    return securitycode;
  }

  public void setSecuritycode(Long securitycode) {
    this.securitycode = securitycode;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "Securitycode{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", securitycode=" + securitycode +
            ", email='" + email + '\'' +
            '}';
  }
}
