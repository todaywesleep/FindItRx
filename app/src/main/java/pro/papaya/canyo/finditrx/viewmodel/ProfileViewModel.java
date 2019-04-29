package pro.papaya.canyo.finditrx.viewmodel;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;

public class ProfileViewModel extends ViewModel {
  public Observable<UserModel> getObservableUser() {
    return FireBaseProfileManager.getInstance().getObservableUser();
  }

  public Single<Void> increaseUserLevel() {
    return FireBaseProfileManager.getInstance().increaseUserLevel();
  }

  public Single<Void> changeNickName(String newNick){
    return FireBaseProfileManager.getInstance().changeUserNickname(newNick);
  }
}
