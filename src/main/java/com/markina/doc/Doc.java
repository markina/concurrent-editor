package com.markina.doc;

/**
 * Created by mmarkina
 */
public interface Doc {
  DocStamp addString(String st, State state);

  DocStamp deleteString(State state, int length);

  DocStamp getLastDoc(State state);

  DocStamp getLastDoc();

  int getLastVersion();
}
