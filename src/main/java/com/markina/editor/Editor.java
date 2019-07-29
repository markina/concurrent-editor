package com.markina.editor;

import com.markina.doc.DocStamp;
import com.markina.publisher.UpdateListener;

/**
 * Created by mmarkina
 */
public interface Editor {
  DocStamp login(UpdateListener updateListener);

  boolean logout(UpdateListener updateListener);

  DocStamp addString(UpdateListener updateListener, String st);

  DocStamp deleteString(UpdateListener updateListener, int length);

  DocStamp getLastDoc(UpdateListener updateListener);
}
