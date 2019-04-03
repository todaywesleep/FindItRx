package pro.papaya.canyo.finditrx.viewmodel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestsModel;

public class QuestsViewModel extends ViewModel {
  public Observable<List<UserQuestsModel>> getObservableUserQuests() {
    return FireBaseProfileManager.getObservableUserTasks();
  }

  public Single<Long> getSingleQuestsTimeStamp() {
    return FireBaseProfileManager.getObservableTimestamp();
  }

  public void createTimestampObject(long timestamp){
    FireBaseProfileManager.createQuestsTimestamp(timestamp);
  }
}
