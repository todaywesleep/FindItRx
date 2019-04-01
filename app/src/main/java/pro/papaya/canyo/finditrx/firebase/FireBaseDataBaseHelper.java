package pro.papaya.canyo.finditrx.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import timber.log.Timber;

public class FireBaseDataBaseHelper {
  private static final String TABLE_USERS = "users";

  public static void addUserWrite(String email, String nickName) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection(TABLE_USERS).add(new UserModel(
        email, nickName
    )).addOnSuccessListener(documentReference -> {
      Timber.d("TEST success: %s", documentReference.toString());
    }).addOnFailureListener(e -> {
      Timber.d("TEST failed: %s", e.getLocalizedMessage());
    });
  }
}
