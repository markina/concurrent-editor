package com.markina.doc;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by mmarkina
 */
public class DocTest {
  @Test
  public void emptyDoc() {
    Doc doc = new DocImpl();
    DocStamp docStamp = doc.getLastDoc();

    assertEquals(0, docStamp.getState().getPosition());
    assertEquals(0, docStamp.getState().getVersion());
    assertEquals("", docStamp.getText());
  }

  @Test
  public void addString() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp = doc.addString("abc", new State(0, 0));

    assertEquals(3, docStamp.getState().getPosition());
    assertEquals(1, docStamp.getState().getVersion());
    assertEquals("abc", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 3)
    ), doc.getModifications());
  }

  @Test
  public void doubleAddString() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abc", new State(0, 0));
    docStamp = doc.addString("1234", new State(3, 1));

    assertEquals(7, docStamp.getState().getPosition());
    assertEquals(2, docStamp.getState().getVersion());
    assertEquals("abc1234", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 3),
      new Modification(3, ChangeType.ADD, 4)
    ), doc.getModifications());
  }

  @Test
  public void addStringWithOldestVersion() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abc", new State(0, 0));
    docStamp = doc.addString("1234", new State(0, 0));

    assertEquals(7, docStamp.getState().getPosition());
    assertEquals(2, docStamp.getState().getVersion());
    assertEquals("abc1234", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 3),
      new Modification(3, ChangeType.ADD, 4)
    ), doc.getModifications());
  }

  @Test
  public void addStringWithVersion() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abc", new State(0, 0));
    doc.addString("DE", new State(3, 1));
    docStamp = doc.addString("1234", new State(2, 1));

    assertEquals(6, docStamp.getState().getPosition());
    assertEquals(3, docStamp.getState().getVersion());
    assertEquals("ab1234cDE", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 3),
      new Modification(3, ChangeType.ADD, 2),
      new Modification(2, ChangeType.ADD, 4)
    ), doc.getModifications());
  }

  @Test
  public void addStringWithZeroVersion() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abc", new State(0, 0));
    doc.addString("DE", new State(3, 1));
    docStamp = doc.addString("1234", new State(0, 0));

    assertEquals(9, docStamp.getState().getPosition());
    assertEquals(3, docStamp.getState().getVersion());
    assertEquals("abcDE1234", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 3),
      new Modification(3, ChangeType.ADD, 2),
      new Modification(5, ChangeType.ADD, 4)
    ), doc.getModifications());
  }

  @Test
  public void deleteString() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abcde", new State(0, 0));
    docStamp = doc.deleteString(new State(2, 1), 1);

    assertEquals(2, docStamp.getState().getPosition());
    assertEquals(2, docStamp.getState().getVersion());
    assertEquals("abde", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 5),
      new Modification(2, ChangeType.DELETE, 1)
    ), doc.getModifications());
  }

  @Test
  public void deleteAll() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abcde", new State(0, 0));
    docStamp = doc.deleteString(new State(0, 1), 5);

    assertEquals(0, docStamp.getState().getPosition());
    assertEquals(2, docStamp.getState().getVersion());
    assertEquals("", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 5),
      new Modification(0, ChangeType.DELETE, 5)
    ), doc.getModifications());
  }

  @Test
  public void deleteSubstring() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("abcde", new State(0, 0));
    docStamp = doc.addString("1234", new State(0, 1));

    assertEquals(4, docStamp.getState().getPosition());
    assertEquals(2, docStamp.getState().getVersion());
    assertEquals("1234abcde", docStamp.getText());

    docStamp = doc.deleteString(new State(2, 2), 5);

    assertEquals(2, docStamp.getState().getPosition());
    assertEquals(3, docStamp.getState().getVersion());
    assertEquals("12de", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 5),
      new Modification(0, ChangeType.ADD, 4),
      new Modification(2, ChangeType.DELETE, 5)
    ), doc.getModifications());
  }

  @Test
  public void deleteStringWithOldVersionWithSpreading() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("ab", new State(0, 0));
    doc.addString("de", new State(2, 1));
    docStamp = doc.addString("1234", new State(2, 2));

    assertEquals(6, docStamp.getState().getPosition());
    assertEquals(3, docStamp.getState().getVersion());
    assertEquals("ab1234de", docStamp.getText());

    docStamp = doc.deleteString(new State(1, 2), 2);

    assertEquals(1, docStamp.getState().getPosition());
    assertEquals(4, docStamp.getState().getVersion());
    assertEquals("ae", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 2),
      new Modification(2, ChangeType.ADD, 2),
      new Modification(2, ChangeType.ADD, 4),
      new Modification(1, ChangeType.DELETE, 6)
    ), doc.getModifications());
  }

  @Test
  public void deleteStringWithOldVersion() {
    DocImpl doc = new DocImpl();
    DocStamp docStamp;
    doc.addString("ab", new State(0, 0));
    doc.addString("de", new State(2, 1));
    docStamp = doc.addString("1234", new State(2, 2));

    assertEquals(6, docStamp.getState().getPosition());
    assertEquals(3, docStamp.getState().getVersion());
    assertEquals("ab1234de", docStamp.getText());

    docStamp = doc.deleteString(new State(1, 2), 1);

    assertEquals(1, docStamp.getState().getPosition());
    assertEquals(4, docStamp.getState().getVersion());
    assertEquals("a1234de", docStamp.getText());

    assertEquals(Arrays.asList(
      new Modification(0, ChangeType.ADD, 0),
      new Modification(0, ChangeType.ADD, 2),
      new Modification(2, ChangeType.ADD, 2),
      new Modification(2, ChangeType.ADD, 4),
      new Modification(1, ChangeType.DELETE, 1)
    ), doc.getModifications());
  }
}
