package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.model.FireBaseResponseModel;

public class FireBaseLoginManger {
  private static final FireBaseLoginManger ourInstance = new FireBaseLoginManger();
  private final FirebaseAuth auth;

  public static FireBaseLoginManger getInstance() {
    return ourInstance;
  }

  private FireBaseResponseModel fireBaseResponse = new FireBaseResponseModel(false, null);
  private Observable<FireBaseResponseModel> fireBaseResponseModel = Observable.just(fireBaseResponse);

  private FireBaseLoginManger() {
    auth = FirebaseAuth.getInstance();
  }

  public Observable<FireBaseResponseModel> getAuthResponseEvent() {
    return fireBaseResponseModel;
  }

  public Observable<FireBaseResponseModel> createRemoteUser(String email, String password) {
    return new Observable<FireBaseResponseModel>() {
      @Override
      protected void subscribeActual(Observer<? super FireBaseResponseModel> observer) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
              observer.onNext(new FireBaseResponseModel(true, null));
            })
            .addOnFailureListener(e -> {
              observer.onNext(new FireBaseResponseModel(false, e.getLocalizedMessage()));
            });
      }
    };
  }
}
