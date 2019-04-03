package pro.papaya.canyo.finditrx.viewmodel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.UserItemModel;

public class QuestsViewModel extends ViewModel {
  public Observable<List<UserItemModel>> getObservableUserQuests() {
    return FireBaseProfileManager.getObservableUserTasks();
  }
}
