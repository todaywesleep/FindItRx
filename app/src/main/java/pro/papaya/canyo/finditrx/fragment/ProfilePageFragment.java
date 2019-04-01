package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.firebase.FireBaseDataBaseHelper;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManger;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import timber.log.Timber;

public class ProfilePageFragment extends BaseFragment {
  private static ProfilePageFragment INSTANCE;

  @BindView(R.id.profile_username)
  TextView userName;

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
    setListeners();
  }

  private void setListeners() {
    FireBaseDataBaseHelper.getObservableUserName(
        FireBaseLoginManger.getInstance().getUserId()
    ).subscribe(new Observer<UserModel>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(UserModel userModel) {
        setLoading(false);
        if (userModel != null && userModel.getNickName() != null) {
          userName.setText(userModel.getNickName());
        }
      }

      @Override
      public void onError(Throwable e) {
        setLoading(false);
        showSnackBar(e.getLocalizedMessage());
      }

      @Override
      public void onComplete() {

      }
    });
  }
}
