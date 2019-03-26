package pro.papaya.canyo.finditrx.firebase;

public class FireBaseLoginManger {
  private static final FireBaseLoginManger ourInstance = new FireBaseLoginManger();

  public static FireBaseLoginManger getInstance() {
    return ourInstance;
  }

  private FireBaseLoginManger() {
  }

  public void createRemoteUser(String email, String password) {
    
  }
}
