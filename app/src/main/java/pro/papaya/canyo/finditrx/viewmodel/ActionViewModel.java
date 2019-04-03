package pro.papaya.canyo.finditrx.viewmodel;

import android.graphics.Bitmap;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.FireBaseLabeler;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;

public class ActionViewModel extends ViewModel {
  public Single<SettingsModel> getSettings() {
    return FireBaseProfileManager.getSettings();
  }

  public void setStableSettings() {
    FireBaseProfileManager.setStableSettings();
  }

  public Single<Boolean> setFlashState(SettingsModel oldModel, boolean isFlashEnabled) {
    return FireBaseProfileManager.setFlashState(oldModel, isFlashEnabled);
  }

  public Single<List<FirebaseVisionImageLabel>> postImageTask(Bitmap bitmap, int rotationDegrees) {
    return FireBaseLabeler.getInstance().postTask(bitmap, rotationDegrees);
  }
}
