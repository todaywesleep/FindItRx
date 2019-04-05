package pro.papaya.canyo.finditrx.viewmodel;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.FireBaseLabeler;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;

public class ActionViewModel extends ViewModel {
  public Observable<SettingsModel> getSettingsReference() {
    return FireBaseProfileManager.getInstance().getObservableSettings();
  }

  public void setStableSettings() {
    FireBaseProfileManager.setStabSettings();
  }


  public Single<Void> setFlashState(SettingsModel oldModel, boolean isFlashEnabled) {
    return FireBaseProfileManager.getInstance().setFlashState(oldModel, isFlashEnabled);
  }

  public Task<List<FirebaseVisionImageLabel>> postImageTask(Bitmap bitmap, int rotationDegrees) {
    return FireBaseLabeler.getInstance().postTask(bitmap, rotationDegrees);
  }
}
