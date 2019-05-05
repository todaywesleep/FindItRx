package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;
import pro.papaya.canyo.finditrx.utils.Constants;

public final class FireBaseUsersManager {
  private static final String COLLECTION_USERS = "users";
  private static final String FIELD_LEVEL = "level";
  private static final String FIELD_BALANCE = "balance";
  private static final String FIELD_FOUNDED_SUBJECTS = "foundedSubjects";

  private static FireBaseUsersManager INSTANCE;
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static FireBaseUsersManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseUsersManager();
    }

    return INSTANCE;
  }

  public Observable<List<UserModel>> getUsersCollection(LeaderBoardPagerModel model) {
    String requiredField = getFieldFromModel(model);

    return Observable.create(emitter -> {
      database.collection(COLLECTION_USERS)
          .orderBy(requiredField, Query.Direction.DESCENDING)
          .limit(Constants.LEADER_BOARD_LIMIT)
          .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              if (!emitter.isDisposed()) {
                List<UserModel> users = queryDocumentSnapshots.toObjects(UserModel.class);
                emitter.onNext(users);
              }
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              if (!emitter.isDisposed()) {
                emitter.onError(e);
              }
            }
          });
    });
  }

  private String getFieldFromModel(LeaderBoardPagerModel model) {
    switch (model) {
      case LEVEL_PAGE: {
        return FIELD_LEVEL;
      }

      case NEW_SUBJECTS_PAGE: {
        return FIELD_FOUNDED_SUBJECTS;
      }

      default: {
        return FIELD_BALANCE;
      }
    }
  }
}
