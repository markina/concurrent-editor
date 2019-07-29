package com.markina.editor;

import com.markina.doc.DocStamp;
import com.markina.publisher.UpdateListener;
import com.markina.publisher.UpdateListenerImpl;
import com.markina.user.UserImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mmarkina
 */
public class EditorTest {
  @Test
  public void oneThreadOneUser() {
    UpdateListener listener = new UpdateListenerImpl(new UserImpl("1"));
    Editor editor = new EditorImpl();
    editor.login(listener);
    DocStamp docStamp;
    editor.addString(listener, "123");
    assertEquals("123", listener.getText());
    assertEquals(3, listener.getState().getPosition());

    editor.addString(listener, "abc");
    assertEquals("123abc", listener.getText());
    assertEquals(6, listener.getState().getPosition());

    assertTrue(listener.changePosition(2));
    assertEquals(2, listener.getState().getPosition());
    editor.deleteString(listener, 2);
    assertEquals("12bc", listener.getText());
  }

  @Test
  public void oneThreadTwoUser() {
    UpdateListener listener1 = new UpdateListenerImpl(new UserImpl("1"));
    UpdateListener listener2 = new UpdateListenerImpl(new UserImpl("2"));
    Editor editor = new EditorImpl();
    editor.login(listener1);
    editor.login(listener2);

    editor.addString(listener1, "123");
    assertEquals("123", listener1.getText());
    assertEquals(3, listener1.getState().getPosition());
    assertEquals("123", listener2.getText());
    assertEquals(3, listener2.getState().getPosition());

    listener2.changePosition(0);
    editor.addString(listener2, "ab");
    assertEquals("ab123", listener1.getText());
    assertEquals(5, listener1.getState().getPosition());
    assertEquals("ab123", listener2.getText());
    assertEquals(2, listener2.getState().getPosition());

    listener1.changePosition(1);
    editor.addString(listener1, "9");
    assertEquals("a9b123", listener1.getText());
    assertEquals(2, listener1.getState().getPosition());
    assertEquals("a9b123", listener2.getText());
    assertEquals(3, listener2.getState().getPosition());

    editor.deleteString(listener1, 2);
    assertEquals("a923", listener1.getText());
    assertEquals(2, listener1.getState().getPosition());
    assertEquals("a923", listener2.getText());
    assertEquals(2, listener2.getState().getPosition());
  }

  @Test
  public void getLast() {
    UpdateListener listener = new UpdateListenerImpl(new UserImpl("1"));
    Editor editor = new EditorImpl();
    editor.login(listener);
    editor.addString(listener, "123");
    assertEquals("123", listener.getText());
    assertEquals(3, listener.getState().getPosition());

    DocStamp docStamp = editor.getLastDoc(listener);
    assertEquals("123", docStamp.getText());
    assertEquals(3, docStamp.getState().getPosition());
    assertEquals(1, docStamp.getState().getVersion());

    listener.changePosition(0);
    docStamp = editor.getLastDoc(listener);
    assertEquals("123", docStamp.getText());
    assertEquals(0, docStamp.getState().getPosition());
    assertEquals(1, docStamp.getState().getVersion());
  }

  @Test
  public void concurrentAdd() throws InterruptedException {
    Editor editor = new EditorImpl();
    List<UpdateListener> listeners = new ArrayList<>();
    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 20; ++i) {
      UpdateListener listener = new UpdateListenerImpl(new UserImpl("user" + i));
      editor.login(listener);
      listeners.add(listener);
      String st = " " + i;
      threads.add(new Thread(() -> editor.addString(listener, st)));
    }

    threads.forEach(Thread::start);
    Thread.sleep(100);

    for (int i = 0; i < 20; i++) {
      UpdateListener listener = listeners.get(i);
      assertEquals(20, listener.getState().getVersion());
      assertTrue("Missed '" + i + "'", listener.getText().contains("" + i));
    }
  }

  @Test
  public void oneThreadAddAndDelete() throws InterruptedException {
    Editor editor = new EditorImpl();
    List<UpdateListener> listeners = new ArrayList<>();

    for (int i = 0; i < 20; ++i) {
      UpdateListener listener = new UpdateListenerImpl(new UserImpl("user" + i));
      editor.login(listener);
      listeners.add(listener);
      String st;
      if (i < 10) {
        st = "  " + i;
      } else {
        st = " " + i;
      }

      if (i % 2 == 0) {
        editor.addString(listener, st);
        listener.changePosition(listener.getState().getPosition() - 3);
        editor.deleteString(listener, 3);
      } else {
        editor.addString(listener, st);
      }
    }

    for (int i = 0; i < 20; i++) {
      UpdateListener listener = listeners.get(i);
      assertEquals(30, listener.getState().getVersion());
      if (i % 2 == 0) {
        assertFalse("Extra '" + i + "'", listener.getText().contains(" " + i));
      } else {
        assertTrue("Missed '" + i + "'", listener.getText().contains(" " + i));
      }
    }
  }

  @Test
  public void concurrentAddAndDelete() throws InterruptedException {
    Editor editor = new EditorImpl();
    List<UpdateListener> listeners = new ArrayList<>();
    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 20; ++i) {
      UpdateListener listener = new UpdateListenerImpl(new UserImpl("user" + i));
      editor.login(listener);
      listeners.add(listener);
      String st;
      if (i < 10) {
        st = "  " + i;
      } else {
        st = " " + i;
      }

      if (i % 2 == 0) {
        threads.add(new Thread(() -> {
          editor.addString(listener, st);
          listener.changePosition(listener.getState().getPosition() - 3);
          editor.deleteString(listener, 3);
        }));
      } else {
        threads.add(new Thread(() -> {
          editor.addString(listener, st);
        }));
      }
    }
    threads.forEach(Thread::run);

    Thread.sleep(100);
    for (int i = 0; i < 20; i++) {
      UpdateListener listener = listeners.get(i);
      assertEquals(30, listener.getState().getVersion());
      if (i % 2 == 0) {
        assertFalse("Extra '" + i + "'", listener.getText().contains(" " + i));
      } else {
        assertTrue("Missed '" + i + "'", listener.getText().contains(" " + i));
      }
    }
  }
}
