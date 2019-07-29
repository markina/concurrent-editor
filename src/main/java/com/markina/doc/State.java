package com.markina.doc;

/**
 * Created by mmarkina
 */
public class State {
  private int position;
  private int version;

  public State(int position, int version) {
    this.position = position;
    this.version = version;
  }

  public int getPosition() {
    return position;
  }

  public int getVersion() {
    return version;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "State{" +
      "position=" + position +
      ", version=" + version +
      '}';
  }
}
