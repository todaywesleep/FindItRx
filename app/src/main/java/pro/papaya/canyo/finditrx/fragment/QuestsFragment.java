package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.UserQuestsAdapter;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestsModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.viewmodel.QuestsViewModel;
import timber.log.Timber;

public class QuestsFragment extends BaseFragment {
  public static QuestsFragment INSTANCE = null;

  public interface QuestFragmentCallback {
    void requestQuests(List<UserQuestsModel> oldQuests, int questsCount);

    void requestRandomQuests(int questCount);
  }

  @BindView(R.id.fragment_quests_rv)
  RecyclerView rvActiveQuests;

  private QuestsViewModel questsViewModel;
  private UserQuestsAdapter adapter;
  private QuestFragmentCallback callback;

  public static QuestsFragment getInstance(QuestFragmentCallback callback) {
    if (INSTANCE == null) {
      QuestsFragment fragment = new QuestsFragment();
      Bundle arguments = new Bundle();
      fragment.setCallback(callback);
      fragment.setArguments(arguments);
      INSTANCE = fragment;
    } else if (callback != INSTANCE.callback) {
      INSTANCE.setCallback(callback);
    }

    return INSTANCE;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.main_fragment_stats, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    questsViewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
    rvActiveQuests.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new UserQuestsAdapter();
    rvActiveQuests.setAdapter(adapter);

    subscribeToUserQuests();
    subscribeToTimestamp();
  }

  public void setCallback(QuestFragmentCallback callback) {
    this.callback = callback;
  }

  private void subscribeToTimestamp() {
    questsViewModel.getSingleQuestsTimeStamp()
        .subscribe(new SingleObserver<Long>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(Long timestamp) {
            Timber.d("TEST timestamp: %s", timestamp);
            if (timestamp != null) {
//              requestQuestsFrom(timestamp);
            } else {
              if (callback != null) {
                callback.requestRandomQuests(Constants.USER_MAX_QUESTS);
              }
            }
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            Timber.e(e);
          }
        });
  }

  private void requestQuestsFrom(long timestamp) {
    long timeDifference = new Date().getTime() - timestamp;
    int differMinutes = (int) (timeDifference / 1000 / 60);
    int questsToRequest = differMinutes / Constants.TIME_TO_QUEST_MINS;
    questsToRequest = questsToRequest >= Constants.USER_MAX_QUESTS
        ? Constants.USER_MAX_QUESTS
        : questsToRequest;
    questsToRequest = Constants.USER_MAX_QUESTS - adapter.getData().size() - questsToRequest;

    if (callback != null) {
      callback.requestQuests(adapter.getData(), questsToRequest);
    }
  }

  private void subscribeToUserQuests() {
    questsViewModel.getObservableUserQuests()
        .subscribe(new Observer<List<UserQuestsModel>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(List<UserQuestsModel> userQuestsModels) {
            logDebug("Get user quests: %s", userQuestsModels);
            adapter.setData(userQuestsModels);
//            if (userQuestsModels.size() < Constants.USER_MAX_QUESTS) {
//
//            }
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }

          @Override
          public void onComplete() {

          }
        });
  }
}
