package pro.papaya.canyo.finditrx.viewmodel;


import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import pro.papaya.canyo.finditrx.firebase.FireBaseItemsManager;
import pro.papaya.canyo.finditrx.model.firebase.ItemModel;

public class QuestsViewModel extends ViewModel {
  public Observable<List<ItemModel>> getAllLabels() {
    return FireBaseItemsManager.getObservableItemsCollection();
  }
}
