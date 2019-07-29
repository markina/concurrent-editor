package com.markina.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmarkina
 */
public class DocImpl implements Doc {
  private String text;
  final private List<Modification> modifications = new ArrayList<>();

  public DocImpl() {
    this.text = "";
    modifications.add(new Modification(0, ChangeType.ADD, 0));
  }

  @Override
  public DocStamp addString(String st, State state) {
    State newState = getMovedState(state, getLastVersion(), false);
    modifications.add(new Modification(newState.getPosition(), ChangeType.ADD, st.length()));
    text = text.substring(0, newState.getPosition()) + st + text.substring(newState.getPosition());
    return new DocStamp(text, new State(newState.getPosition() + st.length(), newState.getVersion() + 1));
  }

  @Override
  public DocStamp deleteString(State state, int length) {
    int lastVersion = getLastVersion();
    State stateFrom = getMovedState(new State(state.getPosition(), state.getVersion()), lastVersion, false);
    State stateTo = getMovedState(new State(state.getPosition() + length, state.getVersion()), lastVersion, true);
    if (stateTo.getPosition() - stateFrom.getPosition() <= 0) {
      return new DocStamp(text, new State(stateFrom.getPosition(), stateFrom.getVersion()));
    }

    modifications.add(new Modification(stateFrom.getPosition(), ChangeType.DELETE, stateTo.getPosition() - stateFrom.getPosition()));
    text = text.substring(0, stateFrom.getPosition()) + text.substring(stateTo.getPosition());
    return new DocStamp(text, new State(stateFrom.getPosition(), stateFrom.getVersion() + 1));
  }

  @Override
  public DocStamp getLastDoc(State state) {
    return new DocStamp(text, getMovedState(state, getLastVersion(), false));
  }

  @Override
  public DocStamp getLastDoc() {
    return new DocStamp(text, new State(0, getLastVersion()));
  }

  @Override
  public int getLastVersion() {
    return modifications.size() - 1;
  }

  private State getMovedState(State state, int lastVersion, boolean withSaveingPlace) {
    int currentPosition = state.getPosition();
    int currentVersion = state.getVersion();
    if (currentVersion == lastVersion) {
      return new State(currentPosition, currentVersion);
    }
    currentVersion++;
    for (; currentVersion <= lastVersion; currentVersion++) {
      Modification modification = modifications.get(currentVersion);
      if (modification.getChangeType() == ChangeType.ADD
        && (!withSaveingPlace && modification.getPosition() <= currentPosition)
        || (withSaveingPlace && modification.getPosition() < currentPosition)) {
        currentPosition += modification.getLength();
        continue;
      }
      if (modification.getChangeType() == ChangeType.DELETE
        && modification.getPosition() < currentPosition
        && modification.getPosition() + modification.getLength() <= currentPosition) {
        currentPosition -= modification.getLength();
        continue;
      }
      if (modification.getChangeType() == ChangeType.DELETE
        && modification.getPosition() < currentPosition
        && modification.getPosition() + modification.getLength() > currentPosition) {
        currentPosition = modification.getPosition();
      }
    }

    return new State(currentPosition, lastVersion);
  }

  List<Modification> getModifications() {
    return modifications;
  }
}
