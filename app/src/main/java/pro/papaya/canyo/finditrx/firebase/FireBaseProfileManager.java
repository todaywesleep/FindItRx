package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class FireBaseProfileManager {
  private static final String TABLE_USERS = "users";
  private static final String TABLE_SETTINGS = "settings";
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

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
            database.collection(TABLE_USERS).document(
                FireBaseLoginManger.getInstance().getUserId()
            ).set(new UserModel(
                FireBaseLoginManger.getInstance().getUserEmail(),
                Constants.STOCK_NICKNAME + integer.toString(),
                FireBaseLoginManger.getInstance().getUserId()
            )).addOnSuccessListener(documentReference -> {
              observer.onSuccess(true);
            }).addOnFailureListener(observer::onError);

            //Don't track settings writing
            database.collection(TABLE_SETTINGS).document(
                FireBaseLoginManger.getInstance().getUserId()
            ).set(SettingsModel.getStabSettings());
          }

          @Override
          public void onError(Throwable e) {
            observer.onError(e);
          }
        });
      }
    };
  }

  public static Observable<UserModel> getObservableUserName() {
    return new Observable<UserModel>() {
      @Override
      protected void subscribeActual(Observer<? super UserModel> observer) {
        database.collection(TABLE_USERS).document(FireBaseLoginManger.getInstance().getUserId())
            .addSnapshotListener((documentSnapshot, e) -> {
              if (e != null) {
                observer.onError(e);
              } else if (documentSnapshot != null) {
                observer.onNext(documentSnapshot.toObject(UserModel.class));
              }
            });
      }
    };
  }

  public static Single<SettingsModel> getSettings() {
    return new Single<SettingsModel>() {
      @Override
      protected void subscribeActual(SingleObserver<? super SettingsModel> observer) {
        database.collection(TABLE_SETTINGS).document(FireBaseLoginManger.getInstance().getUserId())
            .addSnapshotListener(((documentSnapshot, e) -> {
              if (e != null) {
                observer.onError(e);
              } else if (documentSnapshot != null) {
                observer.onSuccess(documentSnapshot.toObject(SettingsModel.class));
              }
            }));
      }
    };
  }

  public static Single<Boolean> setFlashState(SettingsModel oldSettings, boolean isFlashEnabled) {
    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        oldSettings.setFlashEnabled(isFlashEnabled);
        database.collection(TABLE_SETTINGS)
            .document(FireBaseLoginManger.getInstance().getUserId())
            .set(oldSettings)
            .addOnSuccessListener(aVoid -> observer.onSuccess(true))
            .addOnFailureListener(observer::onError);
      }
    };
  }

  public static void setStableSettings() {
    database.collection(TABLE_SETTINGS).document(FireBaseLoginManger.getInstance().getUserId())
        .set(SettingsModel.getStabSettings())
        .addOnSuccessListener(aVoid -> Timber.d("Stable settings"))
        .addOnFailureListener(Timber::e);
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
}
