package pro.papaya.canyo.finditrx.firebase;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Random;

import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import timber.log.Timber;

public class FireBaseItemsManager {
  private static final String TABLE_LABELS = "labels";
  private static final String TABLE_LABELS_LABEL_FIELD = "label";

  private static FireBaseItemsManager INSTANCE;

  public static FireBaseItemsManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseItemsManager();
    }

    return INSTANCE;
  }

  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public Query getItemsCollectionQuery() {
    return database.collection(TABLE_LABELS);
  }

  public void updateItemsCollection(List<QuestModel> oldItems, List<QuestModel> items) {
    for (QuestModel item : items) {
      if (oldItems.isEmpty() || !oldItems.contains(item)) {
        addItemToObjectsList(item);
      }
    }
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
              generateReward()
          );

          FireBaseProfileManager.getInstance().getQuestsReference()
              .document(newQuest.getIdentifier())
              .set(newQuest)
              .addOnFailureListener(e -> Timber.d("Can't request %s quest", newQuest.getIdentifier()))
              .addOnSuccessListener(aVoid -> Timber.d("Quest %s successfully requested", newQuest.getIdentifier()));
        })
        .addOnFailureListener(e -> {
          Timber.e("Can't get user quest reference");
        });
  }

  private int generateReward() {
    Random rand = new Random();
    int reward = rand.nextInt(200);
    reward += 1;

    return reward;
  }

  private static void addItemToObjectsList(QuestModel item) {
    database.collection(TABLE_LABELS).document(item.getLabel().toLowerCase()).set(item);
  }
}
