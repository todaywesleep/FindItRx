package pro.papaya.canyo.finditrx.viewmodel;

import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManager;
import pro.papaya.canyo.finditrx.model.firebase.FireBaseResponseModel;

public class AuthViewModel extends ViewModel {
  public Single<FireBaseResponseModel> signUp(String email, String password) {
    return FireBaseLoginManager.getInstance().createRemoteUser(email, password);
  }

  public Single<FireBaseResponseModel> signIn(String email, String password) {
    return FireBaseLoginManager.getInstance().signInRemote(email, password);
  }

  public Single<Boolean> renewAuth(){
    return FireBaseLoginManager.getInstance().renewAuth();
  }
}
