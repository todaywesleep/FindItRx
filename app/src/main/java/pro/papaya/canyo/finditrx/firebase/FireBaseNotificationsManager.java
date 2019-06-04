package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.NotificationModel;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;

public class FireBaseNotificationsManager {
  private static final String NOTIFICATIONS_COLLECTION = "notifications";

  private static FireBaseNotificationsManager INSTANCE;
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static FireBaseNotificationsManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseNotificationsManager();
    }

    return INSTANCE;
  }

  private Observable<List<NotificationModel>> notificationsCollection = new Observable<List<NotificationModel>>() {
    @Override
    protected void subscribeActual(Observer<? super List<NotificationModel>> observer) {
      database.collection(NOTIFICATIONS_COLLECTION)
          .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              List<NotificationModel> notifications = queryDocumentSnapshots.toObjects(NotificationModel.class);
              observer.onNext(notifications);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };

  public Observable<List<NotificationModel>> getObservableNotificaions() {
    return notificationsCollection;
  }
}
