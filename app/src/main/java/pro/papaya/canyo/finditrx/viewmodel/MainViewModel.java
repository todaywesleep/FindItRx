package pro.papaya.canyo.finditrx.viewmodel;

import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import timber.log.Timber;

public class MainViewModel extends ViewModel {
  public void updateLabelsRemote(List<QuestModel> oldItems, List<QuestModel> newItems) {
    FireBaseItemsManager.getInstance().updateItemsCollection(oldItems, newItems);
  }

  public Query getItemCollectionModel() {
    return FireBaseItemsManager.getInstance().getItemsCollectionQuery();
  }

  public void completeQuests(List<UserQuestModel> completedQuests) {
    for (UserQuestModel quest : completedQuests) {
      Timber.d("Quest %s successfully completed", quest.getIdentifier());
      FireBaseProfileManager.getInstance().completeQuest(quest)
          .addOnSuccessListener(aVoid -> enrollMoney(quest.getReward()));
    }
  }

  public List<UserQuestModel> getCompleteQuests(List<UserQuestModel> userQuests, List<QuestModel> foundAnswers) {
    List<UserQuestModel> completedQuests = new ArrayList<>();

    for (UserQuestModel quest : userQuests) {
      if (foundAnswers.contains(QuestModel.from(quest))) {
        completedQuests.add(quest);
      }
    }

    return completedQuests;
  }

  private void enrollMoney(int amount) {
    FireBaseProfileManager.getInstance().enrollMoney(amount);
  }
}
