package pro.papaya.canyo.finditrx.viewmodel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class QuestsViewModel extends ViewModel {
  public CollectionReference getUserQuestsReference() {
    return FireBaseProfileManager.getInstance().getQuestsReference();
  }

  public Observable<List<QuestModel>> getAllItemsCollection() {
    return FireBaseItemsManager.getInstance().getAllItemsCollection();
  }

  public DocumentReference getLastRequestedQuestReference() {
    return FireBaseProfileManager.getInstance().getTimestampReference();
  }

  public void initLastRequestedQuestTimestamp(long time) {
    FireBaseProfileManager.getInstance().initLastRequestedQuestTimestamp(time);
  }

  public void requestQuest(List<QuestModel> availableQuests) {
    FireBaseItemsManager.getInstance().requestQuest(availableQuests);
  }

  //TODO remove after test
  public Task<Void> completeQuest(UserQuestModel questModel) {
    FireBaseProfileManager.getInstance().enrollMoney(questModel.getReward());
    return FireBaseProfileManager.getInstance().completeQuest(questModel);
  }
}
