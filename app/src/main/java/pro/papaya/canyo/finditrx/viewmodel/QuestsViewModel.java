package pro.papaya.canyo.finditrx.viewmodel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.ViewModel;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class QuestsViewModel extends ViewModel {
  public DocumentReference getUserReference() {
    return FireBaseProfileManager.getInstance().getUserReference();
  }

  public CollectionReference getQuestsReference() {
    return FireBaseProfileManager.getInstance().getQuestsReference();
  }

  public Query getItemCollectionModel() {
    return FireBaseItemsManager.getInstance().getItemsCollectionQuery();
  }

  public DocumentReference getLastRequestedQuestReference() {
    return FireBaseProfileManager.getInstance().getTimestampReference();
  }

  public void initLastRequestedQuestTimestamp(long time) {
    FireBaseProfileManager.getInstance().initLastRequestedQuestTimestamp(time);
  }

  public void requestQuest(List<QuestModel> availableQuests) {
    //Remove snapshot listener
    FireBaseItemsManager.getInstance().requestQuest(availableQuests).remove();
  }

  public CollectionReference getUserQuests() {
    return FireBaseProfileManager.getInstance().getUserQuestsReference();
  }

  public DocumentReference completeQuest(UserQuestModel questModel) {
    return null;
  }
}
