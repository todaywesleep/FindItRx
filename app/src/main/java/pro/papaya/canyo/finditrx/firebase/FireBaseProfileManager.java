package pro.papaya.canyo.finditrx.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;
import pro.papaya.canyo.finditrx.model.firebase.TimestampModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class FireBaseProfileManager {
  private static final String COLLECTION_USERS = "users";
  private static final String COLLECTION_SETTINGS = "settings";
  public static final String SUBCOLLECTION_USER_QUESTS = "quests";
  private static final String SUBCOLLECTION_TIMESTAMP = "timestamp";
  public static final String DOCUMENT_QUEST_TIMESTAMP = "last_requested_quest_time";
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  private static FireBaseProfileManager INSTANCE;

  public static FireBaseProfileManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseProfileManager();
    }

    return INSTANCE;
  }

  public static Single<Boolean> createUserWrite() {
    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        getUsersCollectionLength().subscribe(new SingleObserver<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(Integer integer) {
            database.collection(COLLECTION_USERS).document(getUserId())
                .set(new UserModel(
                    FireBaseLoginManager.getInstance().getUserEmail(),
                    Constants.STOCK_NICKNAME + integer.toString(),
                    getUserId()
                )).addOnSuccessListener(documentReference -> {
              observer.onSuccess(true);
            }).addOnFailureListener(observer::onError);

            //Don't track settings writing
            database.collection(COLLECTION_SETTINGS).document(getUserId())
                .set(SettingsModel.getStabSettings());
          }

          @Override
          public void onError(Throwable e) {
            observer.onError(e);
          }
        });
      }
    };
  }

  public DocumentReference getUserReference() {
    return database.collection(COLLECTION_USERS).document(getUserId());
  }

  public DocumentReference getSettingsReference() {
    return database.collection(COLLECTION_SETTINGS).document(getUserId());
  }

  public CollectionReference getQuestsReference() {
    return getUserReference()
        .collection(SUBCOLLECTION_USER_QUESTS);
  }

  public Task<Void> setFlashState(SettingsModel oldSettings, boolean isFlashEnabled) {
    oldSettings.setFlashEnabled(isFlashEnabled);

    return database.collection(COLLECTION_SETTINGS)
        .document(getUserId())
        .set(oldSettings);
  }

  public static void setStabSettings() {
    database.collection(COLLECTION_SETTINGS).document(getUserId())
        .set(SettingsModel.getStabSettings())
        .addOnSuccessListener(aVoid -> Timber.d("Stab settings"))
        .addOnFailureListener(Timber::e);
  }

  public DocumentReference getTimestampReference() {
    return getUserReference()
        .collection(SUBCOLLECTION_TIMESTAMP)
        .document(DOCUMENT_QUEST_TIMESTAMP);
  }

  public void initLastRequestedQuestTimestamp(long time) {
    getUserReference()
        .collection(SUBCOLLECTION_TIMESTAMP)
        .document(DOCUMENT_QUEST_TIMESTAMP)
        .set(new TimestampModel(
            time
        ));
  }

  private static Single<Integer> getUsersCollectionLength() {
    return new Single<Integer>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Integer> observer) {
        database.collection(COLLECTION_USERS).get()
            .addOnSuccessListener(documentReference ->
                observer.onSuccess(documentReference.size()))
            .addOnFailureListener(observer::onError);
      }
    };
  }

  private static String getUserId() {
    return FireBaseLoginManager.getInstance().getUserId();
  }
}
