package pro.papaya.canyo.finditrx.viewmodel;

import com.google.firebase.firestore.DocumentReference;

import androidx.lifecycle.ViewModel;
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;

public class ProfileViewModel extends ViewModel {
  public DocumentReference getUsernameReference(){
    return FireBaseProfileManager.getInstance().getUserReference();
  }
}
