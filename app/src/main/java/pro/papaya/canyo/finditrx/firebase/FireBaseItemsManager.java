package pro.papaya.canyo.finditrx.firebase;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class FireBaseItemsManager {
  private static final String TABLE_LABELS = "labels";
  private static final String TABLE_LABELS_LABEL_FIELD = "label";

  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();
  private static FireBaseItemsManager INSTANCE;

  public static FireBaseItemsManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseItemsManager();
    }

    return INSTANCE;
  }

  private Observable<List<QuestModel>> allItemsCollection = new Observable<List<QuestModel>>() {
    @Override
    protected void subscribeActual(Observer<? super List<QuestModel>> observer) {
      database.collection(TABLE_LABELS)
          .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              List<QuestModel> questModels = queryDocumentSnapshots.toObjects(QuestModel.class);
              observer.onNext(questModels);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };

  public Observable<List<QuestModel>> getAllItemsCollection() {
    return allItemsCollection;
  }

  public Single<List<QuestModel>> updateItemsCollection(List<QuestModel> oldItems, List<QuestModel> items) {
    return new Single<List<QuestModel>>() {
      @Override
      protected void subscribeActual(SingleObserver<? super List<QuestModel>> observer) {
        List<QuestModel> newQuests = new ArrayList<>();

        for (QuestModel item : items) {
          if (oldItems.isEmpty() || !oldItems.contains(item)) {
            Timber.d("Item %s has been successfully added to library!", item.getIdentifier());
            addItemToObjectsList(item);
            newQuests.add(item);
          }
        }

        observer.onSuccess(newQuests);
      }
    };
  }

  public void requestQuest(List<QuestModel> availableQuests) {
    FireBaseProfileManager.getInstance()
        .getQuestsReference()
        .get()
        .addOnSuccessListener(queryDocumentSnapshots -> {

          Random rand = new Random();
          QuestModel selectedQuest = availableQuests.get(rand.nextInt(availableQuests.size()));
          UserQuestModel newQuest = UserQuestModel.from(
              selectedQuest,
              generateReward(),
              generateRewardExperience()
          );

          FireBaseProfileManager.getInstance().getQuestsReference()
              .document(newQuest.getIdentifier())
              .set(newQuest)
              .addOnFailureListener(e -> Timber.d("Can't request %s quest", newQuest.getIdentifier()))
              .addOnSuccessListener(aVoid -> Timber.d("Quest %s successfully requested", newQuest.getIdentifier()));
        })
        .addOnFailureListener(e -> Timber.e("Can't get user quest reference"));
  }

  private int generateReward() {
    Random rand = new Random();
    int reward = rand.nextInt(Constants.UPPER_BORDER_QUEST_REWARD - Constants.BOTTOM_BORDER_QUEST_REWARD);
    reward += 1 + Constants.BOTTOM_BORDER_QUEST_REWARD;

    return reward;
  }

  private int generateRewardExperience() {
    Random rand = new Random();
    int result = rand.nextInt(Constants.UPPER_BORDER_QUEST_EXP_REWARD - Constants.BOTTOM_BORDER_QUEST_EXP_REWARD);
    result += 1 + Constants.BOTTOM_BORDER_QUEST_EXP_REWARD;

    return result;
  }

  private static void addItemToObjectsList(QuestModel item) {
    database.collection(TABLE_LABELS).document(item.getLabel().toLowerCase()).set(item);
  }
}
