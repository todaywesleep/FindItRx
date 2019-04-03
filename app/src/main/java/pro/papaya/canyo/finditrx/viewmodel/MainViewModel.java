package pro.papaya.canyo.finditrx.viewmodel;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestsModel;

public class MainViewModel extends ViewModel {
  public Observable<List<QuestModel>> getAllLabels() {
    return FireBaseItemsManager.getObservableItemsCollection();
  }

  public void updateLabelsRemote(List<QuestModel> oldItems, List<QuestModel> newItems) {
    FireBaseItemsManager.updateItemsCollection(oldItems, newItems);
  }

  public Single<Boolean> requestQuests(List<QuestModel> allQuests,
                                       List<UserQuestsModel> oldItems,
                                       int questsCount) {
    return FireBaseItemsManager.requestUserQuests(allQuests, oldItems, questsCount);
  }
}
