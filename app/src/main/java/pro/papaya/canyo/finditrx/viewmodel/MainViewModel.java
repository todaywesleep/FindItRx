package pro.papaya.canyo.finditrx.viewmodel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import timber.log.Timber;

public class MainViewModel extends ViewModel {
  //Returns new quests
  public Single<List<QuestModel>> updateLabelsRemote(List<QuestModel> oldItems, List<QuestModel> newItems) {
    return FireBaseItemsManager.getInstance().updateItemsCollection(oldItems, newItems);
  }

  public Observable<List<QuestModel>> getAllQuestsCollection() {
    return FireBaseItemsManager.getInstance().getAllItemsCollection();
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

  public Single<List<UserModel>> getUsersCollection(){
    return FireBaseProfileManager.getInstance().getUsers();
  }

  public void enrollMoney(int amount) {
    FireBaseProfileManager.getInstance().enrollMoney(amount);
  }

  public void enrollExperience(int amount) {
    FireBaseProfileManager.getInstance().enrollExperience(amount);
  }

  public void addFoundQuests(List<QuestModel> quests){
    FireBaseProfileManager.getInstance().addSubjectsFound(quests);
  }
}
