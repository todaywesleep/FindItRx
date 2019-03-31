package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.model.FireBaseResponseModel;

public class FireBaseLoginManger {
  private static final FireBaseLoginManger ourInstance = new FireBaseLoginManger();
  private final FirebaseAuth auth;

  public static FireBaseLoginManger getInstance() {
    return ourInstance;
  }

  private FireBaseLoginManger() {
    auth = FirebaseAuth.getInstance();
  }

  public Single<FireBaseResponseModel> createRemoteUser(String email, String password) {
    return new Single<FireBaseResponseModel>() {
      @Override
      protected void subscribeActual(SingleObserver<? super FireBaseResponseModel> observer) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
              observer.onSuccess(new FireBaseResponseModel(true, null));
            })
            .addOnFailureListener(observer::onError);
      }
    };
  }

  public Single<FireBaseResponseModel> signInRemote(String email, String password) {
    return new Single<FireBaseResponseModel>() {
      @Override
      protected void subscribeActual(SingleObserver<? super FireBaseResponseModel> observer) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
              observer.onSuccess(new FireBaseResponseModel(true, null));
            })
            .addOnFailureListener(observer::onError);
      }
    };
  }
}
