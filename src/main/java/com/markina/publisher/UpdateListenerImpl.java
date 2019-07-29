package com.markina.publisher;

import com.markina.doc.DocStamp;
import com.markina.doc.State;
import com.markina.user.User;

/**
 * Created by mmarkina
 */
public class UpdateListenerImpl implements UpdateListener {
  private final User user;
  private volatile DocStamp docStamp;

  public UpdateListenerImpl(User user) {
    this.user = user;
  }

  @Override
  public State getState() {
    if (docStamp == null) {
      return null;
    }
    return docStamp.getState();
  }

  @Override
  public String getText() {
    return docStamp.getText();
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setDocStamp(DocStamp docStamp) {
    // update page
    this.docStamp = docStamp;
  }

  @Override
  public boolean changePosition(int newPosition) {
    if (newPosition <= docStamp.getText().length()) {
      docStamp.getState().setPosition(newPosition);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "UpdateListenerImpl{" +
      "user=" + user +
      ", docStamp=" + docStamp +
      '}';
  }
}
