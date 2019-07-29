package com.markina.publisher;

import com.markina.doc.Doc;
import com.markina.doc.DocStamp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by mmarkina
 */
public class UpdateManagerImpl implements UpdateManager {
  private final Map<String, UpdateListener> listeners = new HashMap<>();
  private final Doc doc;
  private final CyclicBarrier cyclicBarrier;

  public UpdateManagerImpl(Doc doc) {
    this.doc = doc;
    cyclicBarrier = new CyclicBarrier(1, () ->
      listeners.values().forEach(listener -> {
        if (listener.getState().getVersion() < doc.getLastVersion()) {
          DocStamp docStamp = doc.getLastDoc(listener.getState());
          listener.setDocStamp(docStamp);
        }
      }));
  }

  public boolean login(UpdateListener updateListener) {
    if (listeners.containsKey(updateListener.getUser().getUserName())) {
      System.out.println(String.format("Login with username %s failed", updateListener.getUser().getUserName()));
      return false;
    }
    listeners.put(updateListener.getUser().getUserName(), updateListener);
    return true;
  }

  public boolean logout(UpdateListener updateListener) {
    listeners.remove(updateListener.getUser().getUserName());
    return true;
  }

  public void updateAll() {
    try {
      cyclicBarrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

  Map<String, UpdateListener> getListeners() {
    return listeners;
  }
}
