package pro.papaya.canyo.finditrx.viewmodel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;

public class MainViewModel extends ViewModel {
  public void updateLabelsRemote(List<QuestModel> oldItems, List<QuestModel> newItems) {
    FireBaseItemsManager.getInstance().updateItemsCollection(oldItems, newItems);
  }

  public Task<QuerySnapshot> getItemCollectionModel() {
    return FireBaseItemsManager.getInstance().getItemsCollectionQuery();
  }
}
