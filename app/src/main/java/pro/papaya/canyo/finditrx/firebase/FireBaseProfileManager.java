package pro.papaya.canyo.finditrx.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class FireBaseProfileManager {
  private static final String TABLE_USERS = "users";
  private static final String TABLE_SETTINGS = "settings";
  private static final String TABLE_USER_QUESTS = "quests";
  private static final String TABLE_USER_QUESTS_REWARD_FIELD = "reward";
  private static final String TABLE_USER_TIMESTAMP_FIELD = "questTimestamp";
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
            database.collection(TABLE_USERS).document(getUserId())
                .set(new UserModel(
                    FireBaseLoginManager.getInstance().getUserEmail(),
                    Constants.STOCK_NICKNAME + integer.toString(),
                    getUserId(),
                    null
                )).addOnSuccessListener(documentReference -> {
              observer.onSuccess(true);
            }).addOnFailureListener(observer::onError);

            //Don't track settings writing
            database.collection(TABLE_SETTINGS).document(getUserId())
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
    return database.collection(TABLE_USERS).document(getUserId());
  }

  public DocumentReference getSettingsReference() {
    return database.collection(TABLE_SETTINGS).document(getUserId());
  }

  public CollectionReference getQuestsReference() {
    return getUserReference()
        .collection(TABLE_USER_QUESTS);
  }

  public Task<Void> setFlashState(SettingsModel oldSettings, boolean isFlashEnabled) {
    oldSettings.setFlashEnabled(isFlashEnabled);

    return database.collection(TABLE_SETTINGS)
        .document(getUserId())
        .set(oldSettings);
  }

  public static void setStabSettings() {
    database.collection(TABLE_SETTINGS).document(getUserId())
        .set(SettingsModel.getStabSettings())
        .addOnSuccessListener(aVoid -> Timber.d("Stab settings"))
        .addOnFailureListener(Timber::e);
  }

  public void setTimestamp(long timestamp) {
    database.collection(TABLE_USERS).document(getUserId())
        .addSnapshotListener((documentSnapshot, e) -> {
          if (documentSnapshot != null) {
            UserModel userModel = documentSnapshot.toObject(UserModel.class);

            if (userModel != null) {
              userModel.setQuestTimestamp(timestamp);
            }

            updateUserProfile(userModel);
          }
        });
  }

  private void updateUserProfile(UserModel userModel) {
    database.collection(TABLE_USERS).document(getUserId())
        .set(userModel, SetOptions.mergeFields(TABLE_USER_TIMESTAMP_FIELD));
  }

  public void requestQuest(UserQuestModel quest) {
    Timber.d("Request quest for user: %s", quest);
    database.collection(TABLE_USERS).document(getUserId())
        .collection(TABLE_USER_QUESTS).add(quest);
  }

  private static Single<Integer> getUsersCollectionLength() {
    return new Single<Integer>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Integer> observer) {
        database.collection(TABLE_USERS).get()
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
