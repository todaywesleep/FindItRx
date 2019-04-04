package pro.papaya.canyo.finditrx.viewmodel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class QuestsViewModel extends ViewModel {
  public Observable<List<UserQuestModel>> getObservableUserQuests() {
    return FireBaseProfileManager.getInstance().getObservableUserTasks();
  }

  public Single<Long> setTimestamp(long timestamp) {
    return FireBaseProfileManager.getInstance().setTimestamp(timestamp);
  }

  public Single<Boolean> requestQuests(List<QuestModel> availableQuests, Long timestamp, int oldQuestsCount) {
    return FireBaseItemsManager.getInstance().requestQuests(availableQuests, timestamp, oldQuestsCount);
  }
}
