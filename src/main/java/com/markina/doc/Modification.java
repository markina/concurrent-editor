package com.markina.doc;

import java.util.Objects;

/**
 * Created by mmarkina
 */
public class Modification {
  private int position;
  private ChangeType changeType;
  private int length;

  public Modification(int position, ChangeType changeType, int length) {
    this.position = position;
    this.changeType = changeType;
    this.length = length;
  }

  public int getPosition() {
    return position;
  }

  public ChangeType getChangeType() {
    return changeType;
  }

  public int getLength() {
    return length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Modification that = (Modification) o;
    return position == that.position &&
      length == that.length &&
      changeType == that.changeType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, changeType, length);
  }

  @Override
  public String toString() {
    return "com.markina.doc.Modification{" +
      "position=" + position +
      ", changeType=" + changeType +
      ", length=" + length +
      '}';
  }
}
