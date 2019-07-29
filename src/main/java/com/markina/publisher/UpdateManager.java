package com.markina.publisher;

/**
 * Created by mmarkina
 */
public interface UpdateManager {
  boolean login(UpdateListener updateListener);

  boolean logout(UpdateListener updateListener);

  void updateAll();
}
