package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.model.firebase.FireBaseResponseModel;
import pro.papaya.canyo.finditrx.utils.Constants;

public class FireBaseLoginManager {
  private static FireBaseLoginManager INSTANCE;
  private final FirebaseAuth auth;

  public static FireBaseLoginManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseLoginManager();
    }

    return INSTANCE;
  }

  private FireBaseLoginManager() {
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

  public Single<Boolean> renewAuth() {
    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
          currentUser.getIdToken(true)
              .addOnSuccessListener(getTokenResult -> observer.onSuccess(true))
              .addOnFailureListener(observer::onError);
        } else {
          observer.onSuccess(false);
        }
      }
    };
  }

  public void logout() {
    auth.signOut();
  }

  public String getUserEmail() {
    if (auth.getCurrentUser() != null) {
      return auth.getCurrentUser().getEmail();
    }

    return Constants.EMPTY_STRING;
  }

  public String getUserId() {
    if (auth.getCurrentUser() != null) {
      return auth.getCurrentUser().getUid();
    }

    return Constants.EMPTY_STRING;
  }
}
