package pro.papaya.canyo.finditrx.viewmodel;

import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManger;
import pro.papaya.canyo.finditrx.model.firebase.FireBaseResponseModel;

public class AuthViewModel extends ViewModel {
  public Single<FireBaseResponseModel> signUp(String email, String password) {
    return FireBaseLoginManger.getInstance().createRemoteUser(email, password);
  }

  public Single<FireBaseResponseModel> signIn(String email, String password) {
    return FireBaseLoginManger.getInstance().signInRemote(email, password);
  }

  public Single<Boolean> renewAuth(){
    return FireBaseLoginManger.getInstance().renewAuth();
  }
}
