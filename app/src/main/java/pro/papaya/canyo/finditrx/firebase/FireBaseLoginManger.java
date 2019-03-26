package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.model.FireBaseResponseModel;

public class FireBaseLoginManger {
  private static final FireBaseLoginManger ourInstance = new FireBaseLoginManger();
  private final FirebaseAuth auth;
  private FirebaseUser activeUser;

  public static FireBaseLoginManger getInstance() {
    return ourInstance;
  }

  private FireBaseLoginManger() {
    auth = FirebaseAuth.getInstance();
  }

  public void createRemoteUser(String email,
                               String password,
                               Observer<FireBaseResponseModel> observer) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener(authResult -> {
          activeUser = authResult.getUser();
          observer.onNext(new FireBaseResponseModel(true, null));
        })
        .addOnFailureListener(e -> {
          observer.onNext(new FireBaseResponseModel(false, e.getLocalizedMessage()));
        });
  }
}
