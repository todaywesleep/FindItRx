package pro.papaya.canyo.finditrx.firebase;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.utils.TimeUtils;
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

  public void requestQuests(List<QuestModel> availableQuests, Long timestamp, int oldQuestCount) {
    long unpackedTimestamp = timestamp == null
        ? TimeUtils.getTimestampForFullQuests()
        : timestamp;

    long timestampDifferenceInSecs = (new Date().getTime() - unpackedTimestamp) / 1000;
    long questsToRequest = timestampDifferenceInSecs / Constants.TIME_TO_QUEST_MINS * 60;
    questsToRequest = questsToRequest >= Constants.USER_MAX_QUESTS
        ? Constants.USER_MAX_QUESTS
        : questsToRequest - oldQuestCount;

    Random random = new Random();
    for (int i = 0; i < questsToRequest; i++) {
      UserQuestModel questModel = UserQuestModel.from(
          availableQuests.get(random.nextInt(availableQuests.size())),
          generateReward()
      );

      FireBaseProfileManager.getInstance().requestQuest(questModel);
    }
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
