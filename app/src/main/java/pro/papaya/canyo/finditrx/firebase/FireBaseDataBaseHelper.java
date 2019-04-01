package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.utils.Constants;

public class FireBaseDataBaseHelper {
  private static final String TABLE_USERS = "users";
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static Single<Boolean> createUserWrite(String email) {
    return new Single<Boolean>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        getUsersCollectionLength().subscribe(new SingleObserver<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(Integer integer) {
            database.collection(TABLE_USERS).add(new UserModel(
                email, Constants.STOCK_NICKNAME + integer.toString()
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

  private static Single<Integer> getUsersCollectionLength() {
    return new Single<Integer>() {
      @Override
      protected void subscribeActual(SingleObserver<? super Integer> observer) {
        database.collection(TABLE_USERS).get()
            .addOnSuccessListener(documentReference -> {
              observer.onSuccess(documentReference.size());
            })
            .addOnFailureListener(observer::onError);
      }
    };
  }
}
