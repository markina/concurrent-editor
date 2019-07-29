package com.markina.editor;

import com.markina.doc.Doc;
import com.markina.doc.DocImpl;
import com.markina.doc.DocStamp;
import com.markina.publisher.UpdateListener;
import com.markina.publisher.UpdateManager;
import com.markina.publisher.UpdateManagerImpl;

/**
 * Created by mmarkina
 */
public class EditorImpl implements Editor {
  private UpdateManager updateManager;
  private Doc doc;

  public EditorImpl() {
    this.doc = new DocImpl();
    this.updateManager = new UpdateManagerImpl(doc);
  }

  @Override
  public DocStamp login(UpdateListener updateListener) {
    if (updateManager.login(updateListener)) {
      return getLastDoc(updateListener);
    }
    return null;
  }

  @Override
  public boolean logout(UpdateListener updateListener) {
    return updateManager.logout(updateListener);
  }

  @Override
  public synchronized DocStamp addString(UpdateListener updateListener, String st) {
    DocStamp resultDocStamp = doc.addString(st, updateListener.getState());
    updateListener.setDocStamp(resultDocStamp);
    updateManager.updateAll();
    return resultDocStamp;
  }

  @Override
  public synchronized DocStamp deleteString(UpdateListener updateListener, int length) {
    DocStamp resultDocStamp = doc.deleteString(updateListener.getState(), length);
    updateListener.setDocStamp(resultDocStamp);
    updateManager.updateAll();
    return resultDocStamp;
  }

  @Override
  public DocStamp getLastDoc(UpdateListener updateListener) {
    DocStamp lastDoc;
    if (updateListener.getState() == null) {
      lastDoc = doc.getLastDoc();
    } else {
      lastDoc = doc.getLastDoc(updateListener.getState());
    }
    updateListener.setDocStamp(lastDoc);
    return lastDoc;
  }
}
