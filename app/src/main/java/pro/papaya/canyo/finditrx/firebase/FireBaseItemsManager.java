package pro.papaya.canyo.finditrx.firebase;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestsModel;

public class FireBaseItemsManager {
  private static final String TABLE_LABELS = "labels";
  private static final String TABLE_LABELS_LABEL_FIELD = "label";

  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static Observable<List<QuestModel>> getObservableItemsCollection() {
    return new Observable<List<QuestModel>>() {
      @Override
      protected void subscribeActual(Observer<? super List<QuestModel>> observer) {
        database.collection(TABLE_LABELS).orderBy(TABLE_LABELS_LABEL_FIELD)
            .addSnapshotListener((queryDocumentSnapshots, e) -> {
              if (queryDocumentSnapshots != null) {
                observer.onNext(queryDocumentSnapshots.toObjects(QuestModel.class));
              } else if (e != null) {
                observer.onError(e);
              }
            });
      }
    };
  }

  public static void updateItemsCollection(List<QuestModel> oldItems, List<QuestModel> items) {
    for (QuestModel item : items) {
      if (oldItems.isEmpty() || !oldItems.contains(item)) {
        addItemToObjectsList(item);
      }
    }
  }

  public static Single<Boolean> requestUserQuests(
      List<QuestModel> allQuests,
      List<UserQuestsModel> oldItems,
      int questsToRequest) {

    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        for (UserQuestsModel userQuest : oldItems) {
          QuestModel questModel = QuestModel.from(userQuest);
          allQuests.remove(questModel);
        }

        if (!allQuests.isEmpty()) {
          for (int i = 0; i < questsToRequest; i++) {
            int index = new Random().nextInt(allQuests.size());
            QuestModel modelToUser = allQuests.get(index);
            UserQuestsModel preparedObject = UserQuestsModel.from(modelToUser, generateReward());

            FireBaseProfileManager.createQuestForUser(preparedObject);
          }

          FireBaseProfileManager.createQuestsTimestamp(new Date().getTime());
        }

        observer.onSuccess(true);
      }
    };
  }

  private static int generateReward() {
    Random rand = new Random();
    int reward = rand.nextInt(200);
    reward += 1;

    return reward;
  }

  private static void addItemToObjectsList(QuestModel item) {
    database.collection(TABLE_LABELS).document(item.getLabel().toLowerCase()).set(item);
  }
}
