package com.markina.publisher;

import com.markina.doc.Doc;
import com.markina.doc.DocImpl;
import com.markina.doc.DocStamp;
import com.markina.doc.State;
import com.markina.user.UserImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mmarkina
 */
public class UpdateManagerTest {
  @Test
  public void login() {
    UpdateManagerImpl updateManager = new UpdateManagerImpl(new DocImpl());
    UpdateListenerImpl user = new UpdateListenerImpl(new UserImpl("name"));
    assertTrue(updateManager.login(user));

    assertEquals(1, updateManager.getListeners().size());
    assertTrue(updateManager.getListeners().containsKey("name"));
    assertEquals(user, updateManager.getListeners().get("name"));
  }

  @Test
  public void doubleLogin() {
    UpdateManagerImpl updateManager = new UpdateManagerImpl(new DocImpl());
    UserImpl user = new UserImpl("name");
    UpdateListenerImpl listener = new UpdateListenerImpl(user);
    UpdateListenerImpl listener2 = new UpdateListenerImpl(user);
    assertTrue(updateManager.login(listener));
    assertFalse(updateManager.login(listener2));

    assertEquals(1, updateManager.getListeners().size());
    assertTrue(updateManager.getListeners().containsKey("name"));
    assertEquals(listener, updateManager.getListeners().get("name"));
  }

  @Test
  public void severalLogin() {
    UpdateManagerImpl updateManager = new UpdateManagerImpl(new DocImpl());
    UpdateListenerImpl listener = new UpdateListenerImpl(new UserImpl("name"));
    UpdateListenerImpl listener2 = new UpdateListenerImpl(new UserImpl("name2"));
    assertTrue(updateManager.login(listener));
    assertTrue(updateManager.login(listener2));

    assertEquals(2, updateManager.getListeners().size());
    assertTrue(updateManager.getListeners().containsKey("name"));
    assertTrue(updateManager.getListeners().containsKey("name2"));
    assertEquals(listener, updateManager.getListeners().get("name"));
    assertEquals(listener2, updateManager.getListeners().get("name2"));
  }

  @Test
  public void loginLogout() {
    UpdateManagerImpl updateManager = new UpdateManagerImpl(new DocImpl());
    UpdateListenerImpl user = new UpdateListenerImpl(new UserImpl("name"));
    assertTrue(updateManager.login(user));
    assertTrue(updateManager.logout(user));
    assertEquals(0, updateManager.getListeners().size());
  }

  @Test
  public void checkUpdate() {
    UpdateManagerImpl updateManager = new UpdateManagerImpl(new DocImpl());
    UpdateListenerImpl user = new UpdateListenerImpl(new UserImpl("name"));
    UpdateListenerImpl user2 = new UpdateListenerImpl(new UserImpl("name2"));
    assertTrue(updateManager.login(user));
    assertTrue(updateManager.login(user2));

    Doc doc = new DocImpl();
    DocStamp docStamp = doc.addString("abc", new State(0, 0));

    user.setDocStamp(docStamp);
    user2.setDocStamp(docStamp);
    assertEquals("abc", user.getText());
    assertEquals("abc", user2.getText());
  }
}
