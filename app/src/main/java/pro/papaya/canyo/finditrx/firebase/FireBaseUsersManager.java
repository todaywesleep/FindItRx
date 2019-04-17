package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;

public final class FireBaseUsersManager {
  private static final String COLLECTION_USERS = "users";

  private static FireBaseUsersManager INSTANCE;
  private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

  public static FireBaseUsersManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseUsersManager();
    }

    return INSTANCE;
  }

  public Observable<List<QuestModel>> getUsersCollection(LeaderBoardPagerModel model) {
    
    return new Observable<List<QuestModel>>() {
      @Override
      protected void subscribeActual(Observer<? super List<QuestModel>> observer) {

      }
    };
  }
}
