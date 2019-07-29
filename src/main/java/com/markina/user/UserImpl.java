package com.markina.user;

/**
 * Created by mmarkina
 */
public class UserImpl implements User {
  private String username;

  public UserImpl(String username) {
    this.username = username;
  }

  @Override
  public String getUserName() {
    return username;
  }

  @Override
  public String toString() {
    return "UserImpl{" +
      "username='" + username + '\'' +
      '}';
  }
}
