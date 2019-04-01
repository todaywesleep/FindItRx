package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.utils.Constants;

public class FireBaseDataBaseHelper {
  private static final String TABLE_USERS = "users";
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static Single<Boolean> createUserWrite(String email, String id) {
    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        getUsersCollectionLength().subscribe(new SingleObserver<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(Integer integer) {
            database.collection(TABLE_USERS).document(id)
                .set(new UserModel(
                    email, Constants.STOCK_NICKNAME + integer.toString(), id
                )).addOnSuccessListener(documentReference -> {
              observer.onSuccess(true);
            }).addOnFailureListener(observer::onError);
          }

          @Override
          public void onError(Throwable e) {
            observer.onError(e);
          }
        });
      }
    };
  }

  public static Observable<UserModel> getObservableUserName(String userId) {
    return new Observable<UserModel>() {
      @Override
      protected void subscribeActual(Observer<? super UserModel> observer) {
        database.collection(TABLE_USERS).document(userId)
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
