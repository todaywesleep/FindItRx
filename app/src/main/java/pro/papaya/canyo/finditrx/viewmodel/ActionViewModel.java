package pro.papaya.canyo.finditrx.viewmodel;

import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseDataBaseHelper;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;

public class ActionViewModel extends ViewModel {
  public Single<SettingsModel> getSettings() {
    return FireBaseDataBaseHelper.getSettings();
  }

  public void setStableSettings() {
    FireBaseDataBaseHelper.setStableSettings();
  }

  public Single<Boolean> setFlashState(SettingsModel oldModel, boolean isFlashEnabled){
    return FireBaseDataBaseHelper.setFlashState(oldModel, isFlashEnabled);
  }

//  public Single<Boolean> setFlashMode(boolean isFlashModeEnabled){
//    return FireBaseDataBaseHelper
//  }
}
