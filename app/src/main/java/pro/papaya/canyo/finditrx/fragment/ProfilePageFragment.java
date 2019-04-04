package pro.papaya.canyo.finditrx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.activity.AuthActivity;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManager;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.viewmodel.ProfileViewModel;

public class ProfilePageFragment extends BaseFragment {
  private static ProfilePageFragment INSTANCE;

  @BindView(R.id.profile_username)
  TextView userName;
  @BindView(R.id.profile_logout)
  Button logout;

  private ProfileViewModel profileViewModel;

  public static ProfilePageFragment getInstance() {
    if (INSTANCE == null) {
      ProfilePageFragment fragment = new ProfilePageFragment();
      Bundle arguments = new Bundle();
      fragment.setArguments(arguments);
      INSTANCE = fragment;
    }

    return INSTANCE;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.main_fragment_profile, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    setListeners();
    setViewListeners();
  }

  private void setViewListeners() {
    logout.setOnClickListener(v -> {
      FireBaseLoginManager.getInstance().logout();

      Intent navigationIntent = new Intent(getContext(), AuthActivity.class);
      navigationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(navigationIntent);
    });
  }

  private void setListeners() {
    profileViewModel.getUsernameReference()
        .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            UserModel userModel = documentSnapshot.toObject(UserModel.class);
            if (userModel != null) {
              userName.setText(userModel.getNickName());
              logDebug("User model updated");
            }
          }

          @Override
          public void onError(FirebaseFirestoreException e) {
            setLoading(false);
            showSnackBar(e.getLocalizedMessage());
          }
        });
  }
}
