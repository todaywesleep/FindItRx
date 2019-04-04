package pro.papaya.canyo.finditrx.listener;

import com.google.firebase.firestore.FirebaseFirestoreException;

public interface ISnapshotUpdate <T>{
  void onSuccess (T t);
  void onError (FirebaseFirestoreException e);
}
