package com.markina.publisher;

import com.markina.doc.DocStamp;
import com.markina.doc.State;
import com.markina.user.User;

/**
 * Created by mmarkina
 */
public interface UpdateListener {
  State getState();

  String getText();

  User getUser();

  boolean changePosition(int newPosition);

  void setDocStamp(DocStamp docStamp);
}
