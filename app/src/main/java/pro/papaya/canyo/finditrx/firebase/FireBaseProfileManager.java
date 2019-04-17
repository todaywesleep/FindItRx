package pro.papaya.canyo.finditrx.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;
import pro.papaya.canyo.finditrx.model.firebase.TimestampModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.CalculatorUtils;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class FireBaseProfileManager {
  private static final String COLLECTION_USERS = "users";
  private static final String COLLECTION_SETTINGS = "settings";
  public static final String SUBCOLLECTION_USER_QUESTS = "quests";
  private static final String SUBCOLLECTION_TIMESTAMP = "timestamp";
  private static final String SUBCOLLECTION_FOUND_SUBJECTS = "found_subjects";
  public static final String DOCUMENT_QUEST_TIMESTAMP = "last_requested_quest_time";
  public static final String FIELD_BALANCE = "balance";
  public static final String FIELD_EXPERIENCE = "experience";
  public static final String FIELD_LEVEL = "level";
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  private static FireBaseProfileManager INSTANCE;
  private Observable<UserModel> user = new Observable<UserModel>() {
    @Override
    protected void subscribeActual(Observer<? super UserModel> observer) {
      getUserReference()
          .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              UserModel userModel = documentSnapshot.toObject(UserModel.class);
              observer.onNext(userModel);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };
  private Observable<SettingsModel> settings = new Observable<SettingsModel>() {
    @Override
    protected void subscribeActual(Observer<? super SettingsModel> observer) {
      database
          .collection(COLLECTION_SETTINGS)
          .document(getUserId())
          .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              SettingsModel settingsModel = documentSnapshot.toObject(SettingsModel.class);
              observer.onNext(settingsModel);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };
  private Observable<List<UserQuestModel>> userQuests = new Observable<List<UserQuestModel>>() {
    @Override
    protected void subscribeActual(Observer<? super List<UserQuestModel>> observer) {
      getQuestsReference()
          .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              List<UserQuestModel> userQuestModels = queryDocumentSnapshots.toObjects(UserQuestModel.class);
              observer.onNext(userQuestModels);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };
  private Observable<TimestampModel> timestamp = new Observable<TimestampModel>() {
    @Override
    protected void subscribeActual(Observer<? super TimestampModel> observer) {
      getUserReference()
          .collection(SUBCOLLECTION_TIMESTAMP)
          .document(DOCUMENT_QUEST_TIMESTAMP)
          .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              TimestampModel timestampModel = documentSnapshot.toObject(TimestampModel.class);
              observer.onNext(timestampModel);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              observer.onError(e);
            }
          });
    }
  };

  public Observable<UserModel> getObservableUser() {
    return user;
  }

  public Observable<SettingsModel> getObservableSettings() {
    return settings;
  }

  public Observable<List<UserQuestModel>> getObservableQuests() {
    return userQuests;
  }

  public Observable<TimestampModel> getObservableTimestamp() {
    return timestamp;
  }

  public static FireBaseProfileManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseProfileManager();
    }

    return INSTANCE;
  }

  public Single<List<UserModel>> getUsers() {
    return new Single<List<UserModel>>() {
      @Override
      protected void subscribeActual(SingleObserver<? super List<UserModel>> observer) {
        database.collection(COLLECTION_USERS)
            .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot querySnapshot) {
                List<UserModel> userModels = new ArrayList<>(querySnapshot.toObjects(UserModel.class));
                observer.onSuccess(userModels);
              }

              @Override
              public void onError(FirebaseFirestoreException e) {
                observer.onError(e);
              }
            });
      }
    };
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
                    getUserId(),
                    0,
                    0
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

  public Single<Void> increaseUserLevel() {
    return new Single<Void>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Void> observer) {
        getUserReference().get()
            .addOnSuccessListener(documentSnapshot -> {
              UserModel user = documentSnapshot.toObject(UserModel.class);
              if (user != null) {
                int newUserLevel = user.getLevel() + 1;
                int experience = Math.abs(CalculatorUtils.getRestExperience(
                    user.getLevel(),
                    user.getExperience()
                ));
                getUserReference().update(FIELD_LEVEL, newUserLevel);
                getUserReference().update(FIELD_EXPERIENCE, experience);
              }
            });
      }
    };
  }

  public DocumentReference getUserReference() {
    return database.collection(COLLECTION_USERS).document(getUserId());
  }

  public CollectionReference getQuestsReference() {
    return getUserReference()
        .collection(SUBCOLLECTION_USER_QUESTS);
  }

  public Single<Void> setFlashState(SettingsModel oldSettings, boolean isFlashEnabled) {
    return new Single<Void>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Void> observer) {
        oldSettings.setFlashEnabled(isFlashEnabled);

        database.collection(COLLECTION_SETTINGS)
            .document(getUserId())
            .set(oldSettings)
            .addOnSuccessListener(aVoid -> observer.onSuccess(null))
            .addOnFailureListener(observer::onError);
      }
    };
  }

  public static void setStabSettings() {
    database.collection(COLLECTION_SETTINGS).document(getUserId())
        .set(SettingsModel.getStabSettings())
        .addOnSuccessListener(aVoid -> Timber.d("Stab settings"))
        .addOnFailureListener(Timber::e);
  }

  public void initLastRequestedQuestTimestamp(long time) {
    getUserReference()
        .collection(SUBCOLLECTION_TIMESTAMP)
        .document(DOCUMENT_QUEST_TIMESTAMP)
        .set(new TimestampModel(
            time
        ));
  }

  public Task<Void> completeQuest(UserQuestModel userQuest) {
    return getUserQuestsReference()
        .document(userQuest.getIdentifier())
        .delete();
  }

  public void enrollMoney(int amount) {
    getUserReference()
        .get()
        .addOnSuccessListener(documentSnapshot -> {
          UserModel user = documentSnapshot.toObject(UserModel.class);
          if (user != null) {
            long newBalance = user.getBalance() + amount;
            getUserReference().update(FIELD_BALANCE, newBalance)
                .addOnSuccessListener(aVoid -> Timber.d("User balance update and equals %s", newBalance))
                .addOnFailureListener(e -> Timber.d("Can't update user's balance"));
          }
        });
  }

  public void enrollExperience(int amount) {
    getUserReference()
        .get()
        .addOnSuccessListener(documentSnapshot -> {
          UserModel user = documentSnapshot.toObject(UserModel.class);
          if (user != null) {
            long newExperience = user.getExperience() + amount;
            getUserReference().update(FIELD_EXPERIENCE, newExperience)
                .addOnSuccessListener(aVoid -> Timber.d("User experience updated and equals: %s", newExperience))
                .addOnFailureListener(aVoid -> Timber.d("Can't update user experience"));
          }
        });
  }

  public void addSubjectsFound(List<QuestModel> quests) {
    if (quests != null && !quests.isEmpty()) {
      for (QuestModel quest : quests) {
        getUserReference()
            .collection(SUBCOLLECTION_FOUND_SUBJECTS)
            .document(quest.getIdentifier())
            .set(quest);
      }
    }
  }

  public CollectionReference getUserQuestsReference() {
    return getQuestsReference();
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
