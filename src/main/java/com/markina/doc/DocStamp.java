package com.markina.doc;

/**
 * Created by mmarkina
 */
public class DocStamp {
  private String text;
  private State state;

  public DocStamp(String text, State state) {
    this.text = text;
    this.state = state;
  }

  public String getText() {
    return text;
  }

  public State getState() {
    return state;
  }

  @Override
  public String toString() {
    return "DocStamp{" +
      "text='" + text + '\'' +
      ", state=" + state +
      '}';
  }
}
