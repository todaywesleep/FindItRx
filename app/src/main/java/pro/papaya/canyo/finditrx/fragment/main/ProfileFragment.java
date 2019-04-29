package pro.papaya.canyo.finditrx.fragment.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.activity.AuthActivity;
import pro.papaya.canyo.finditrx.dialog.ChangeNicknameDialog;
import pro.papaya.canyo.finditrx.dialog.NotEnoughMoneyDialog;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManager;
import pro.papaya.canyo.finditrx.listener.ShortObserver;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.utils.CalculatorUtils;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.viewmodel.ProfileViewModel;

public class ProfileFragment extends BaseFragment
    implements ChangeNicknameDialog.ChangeNicknameDialogCallback {
  public interface ProfileFragmentCallback {
    void onLeaderBoardRequest();
  }

  @BindView(R.id.profile_username)
  TextView userName;
  @BindView(R.id.profile_level)
  TextView level;
  @BindView(R.id.profile_balance)
  TextView balance;
  @BindView(R.id.profile_exp)
  TextView experience;
  @BindView(R.id.profile_rest_exp)
  TextView restExperience;
  @BindView(R.id.profile_exp_progress)
  ProgressBar expProgress;
  @BindView(R.id.profile_logout)
  Button logout;
  @BindView(R.id.profile_leader_board)
  Button leaderBoard;
  @BindView(R.id.profile_edit_nickname)
  ImageButton editProfileName;

  private ProfileViewModel profileViewModel;
  private ProfileFragmentCallback callback;
  private UserModel currentUserModel;

  public static ProfileFragment getNewInstance(ProfileFragmentCallback callback) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle arguments = new Bundle();
    fragment.setArguments(arguments);
    fragment.setRetainInstance(true);
    fragment.setCallback(callback);

    return fragment;
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
    initViews();
    setSubscriptions();
    setViewListeners();
  }

  @Override
  public void onPayClick(String newUserName) {
    setLoading(true);

    if (currentUserModel != null) {
      if (currentUserModel.getBalance() > Constants.PRICE_CHANGE_NICKNAME) {
        profileViewModel.changeNickName(newUserName)
            .subscribe(new SingleObserver<Void>() {
              @Override
              public void onSubscribe(Disposable d) {
              }

              @Override
              public void onSuccess(Void aVoid) {
                setLoading(false);
                logDebug("Nickname changed to %s", newUserName);
              }

              @Override
              public void onError(Throwable e) {
                setLoading(false);
                showSnackBar(e.getLocalizedMessage());
                logError(e);
              }
            });
      } else {
        setLoading(false);
        new NotEnoughMoneyDialog(getContext()).show();
      }
    } else {
      setLoading(false);
      showSnackBar("User model is null");
      logError(new Throwable("User model is null"));
    }
  }

  private void initViews() {
    editProfileName.setOnClickListener(v -> {
      if (getContext() != null) {
        new ChangeNicknameDialog(getContext(),
            userName.getText().toString(),
            this).show();
      }
    });
  }

  private void setCallback(ProfileFragmentCallback callback) {
    this.callback = callback;
  }

  private void setViewListeners() {
    logout.setOnClickListener(v -> {
      FireBaseLoginManager.getInstance().logout();

      Intent navigationIntent = new Intent(getContext(), AuthActivity.class);
      navigationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(navigationIntent);
    });

    leaderBoard.setOnClickListener(v -> {
      if (callback != null) {
        callback.onLeaderBoardRequest();
      }
    });
  }

  private void setSubscriptions() {
    profileViewModel.getObservableUser()
        .subscribe(new ShortObserver<UserModel>() {
          @SuppressLint("SetTextI18n")
          @Override
          public void onNext(UserModel userModel) {
            if (userModel != null) {
              currentUserModel = userModel;
              int restExperienceValue = CalculatorUtils.getRestExperience(
                  userModel.getLevel(), userModel.getExperience()
              );
              userName.setText(userModel.getNickName());
              balance.setText(String.format(Locale.getDefault(),
                  getString(R.string.fragment_quests_balance),
                  userModel.getBalance()));
              experience.setText(String.valueOf(userModel.getExperience()));
              level.setText(String.format(Locale.getDefault(),
                  getString(R.string.fragment_quests_level),
                  userModel.getLevel())
              );
              restExperience.setText(String.valueOf(restExperienceValue));
              expProgress.setMax(userModel.getExperience() + restExperienceValue);
              expProgress.setProgress(userModel.getExperience());

              if (restExperienceValue <= 0) {
                profileViewModel.increaseUserLevel()
                    .subscribe(new SingleObserver<Void>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onSuccess(Void aVoid) {
                        logDebug("User level increased");
                      }

                      @Override
                      public void onError(Throwable e) {
                        showSnackBar(e.getLocalizedMessage());
                        logError(e);
                      }
                    });
              }

              logDebug("Profile applied");
            }
          }

          @Override
          public void onError(Throwable e) {
            setLoading(false);
            showSnackBar(e.getLocalizedMessage());
          }
        });
  }
}
