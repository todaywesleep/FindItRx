package pro.papaya.canyo.finditrx.firebase;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;

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

  private Observable<List<QuestModel>> itemsCollection = new Observable<List<QuestModel>>() {
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

  public Observable<List<QuestModel>> getObservableItemsCollection() {
    return itemsCollection;
  }

  public static void updateItemsCollection(List<QuestModel> oldItems, List<QuestModel> items) {
    for (QuestModel item : items) {
      if (oldItems.isEmpty() || !oldItems.contains(item)) {
        addItemToObjectsList(item);
      }
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
