package pro.papaya.canyo.finditrx.listener;


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public abstract class ExtendedEventListener<T> implements EventListener<T>, ISnapshotUpdate<T> {
  @Override
  public void onEvent(@Nullable T t, @Nullable FirebaseFirestoreException e) {
    if (t != null) {
      onSuccess(t);
    } else if (e != null) {
      onError(e);
    }
  }

  @Override
  abstract public void onSuccess(T t);

  @Override
  abstract public void onError(FirebaseFirestoreException e);
}
