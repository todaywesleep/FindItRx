package pro.papaya.canyo.finditrx.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.TimestampModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class QuestsViewModel extends ViewModel {
  public Observable<List<UserQuestModel>> getUserQuestsObservable() {
    return FireBaseProfileManager.getInstance().getObservableQuests();
  }

  public Observable<List<QuestModel>> getAllItemsObservable() {
    return FireBaseItemsManager.getInstance().getAllItemsCollection();
  }

  public Observable<TimestampModel> getTimestampObservable() {
    return FireBaseProfileManager.getInstance().getObservableTimestamp();
  }

  public void initLastRequestedQuestTimestamp(long time) {
    FireBaseProfileManager.getInstance().initLastRequestedQuestTimestamp(time);
  }

  public void requestQuest(List<QuestModel> availableQuests) {
    FireBaseItemsManager.getInstance().requestQuest(availableQuests);
  }

  public Single<Boolean> rejectQuest(QuestModel questToReject) {
    return FireBaseItemsManager.getInstance().rejectQuest(questToReject);
  }
}
