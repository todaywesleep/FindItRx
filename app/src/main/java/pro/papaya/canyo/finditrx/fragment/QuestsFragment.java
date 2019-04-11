package pro.papaya.canyo.finditrx.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.UserQuestsAdapter;
import pro.papaya.canyo.finditrx.listener.CutedObserver;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.TimestampModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.utils.TimeUtils;
import pro.papaya.canyo.finditrx.viewmodel.QuestsViewModel;

public class QuestsFragment extends BaseFragment implements UserQuestsAdapter.QuestCallback {
  public interface QuestFragmentCallback {
  }

  @BindView(R.id.fragment_quests_rv)
  RecyclerView rvActiveQuests;
  @BindView(R.id.quests_tv_time_label)
  TextView tvRemainingTimeLabel;

  private QuestsViewModel questsViewModel;
  private UserQuestsAdapter adapter;
  private QuestFragmentCallback callback;
  private List<QuestModel> availableQuests = new ArrayList<>();

  private Long lastQuestTimestamp;
  private ListenerRegistration timestampListener;
  private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

  public static QuestsFragment getNewInstance(QuestFragmentCallback callback) {
    QuestsFragment fragment = new QuestsFragment();
    Bundle arguments = new Bundle();
    fragment.setCallback(callback);
    fragment.setArguments(arguments);
    fragment.setRetainInstance(true);

    return fragment;
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
    rvActiveQuests.addItemDecoration(getItemDecorator());
    adapter = new UserQuestsAdapter();
    adapter.setCallback(this);
    rvActiveQuests.setAdapter(adapter);

    subscribeToAllLabels();
    subscribeToUserQuests();
  }

  @Override
  public void onQuestClicked(UserQuestModel quest) {
    //TODO remove after testing
    questsViewModel.completeQuest(quest)
        .addOnSuccessListener(aVoid -> {
          logDebug("Quest %s successfully completed", quest.getIdentifier());
        })
        .addOnFailureListener(e -> {
          showSnackBar(e.getLocalizedMessage());
          logError(e);
        });
  }

  public void setCallback(QuestFragmentCallback callback) {
    this.callback = callback;
  }

  public List<UserQuestModel> getUserQuests() {
    return adapter.getData();
  }

  private RecyclerView.ItemDecoration getItemDecorator() {
    return new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
          //TODO move into dimens
          outRect.top = 16;
        }

        outRect.bottom = 16;
      }
    };
  }

  private void subscribeToAllLabels() {
    questsViewModel.getAllItemsObservable()
        .subscribe(new CutedObserver<List<QuestModel>>() {
          @Override
          public void onNext(List<QuestModel> questModels) {
            availableQuests.clear();
            availableQuests.addAll(questModels);

            logDebug("Items collection updated");
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private void subscribeToQuestTimestamp() {
    questsViewModel.getTimestampObservable()
        .subscribe(new CutedObserver<TimestampModel>() {
          @Override
          public void onNext(TimestampModel timestampModel) {
            if (timestampModel != null) {
              long timeToNextQuest = getTimeToNextQuest(timestampModel.getLastRequestedQuestTime());
              long fullTimeToQuest = TimeUtils.minsToMillis(Constants.TIME_TO_QUEST_MINS);

              if (timeToNextQuest >= 0 && timeToNextQuest <= fullTimeToQuest) {
                initTimerMillis(timeToNextQuest);
              } else if (timeToNextQuest < 0 && !adapter.isAdapterFullFilled()) {
                tvRemainingTimeLabel.setText(getString(R.string.loading));
                questsViewModel.requestQuest(availableQuests);
              } else {
                initTimerMillis(null);
              }

              lastQuestTimestamp = timestampModel.getLastRequestedQuestTime();
            } else {
              long lastRequestedQuestTime = new Date().getTime();
              lastQuestTimestamp = lastRequestedQuestTime;
              logDebug("Init last requested quest: %s", lastRequestedQuestTime);
              questsViewModel.initLastRequestedQuestTimestamp(lastRequestedQuestTime);
            }
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private void subscribeToUserQuests() {
    questsViewModel.getUserQuestsObservable()
        .subscribe(new CutedObserver<List<UserQuestModel>>() {
          @Override
          public void onNext(List<UserQuestModel> userQuestModels) {
            if (adapter.isInitialized()) {
              if (userQuestModels.size() >= Constants.USER_MAX_QUESTS
                  || (adapter.getItemCount() > userQuestModels.size()
                  && adapter.getItemCount() >= Constants.USER_MAX_QUESTS)) {
                questsViewModel.initLastRequestedQuestTimestamp(new Date().getTime());
                initTimerMillis(null);
              }
            }

            adapter.setData(userQuestModels);

            if (timestampListener != null) {
              timestampListener.remove();
            }
            subscribeToQuestTimestamp();
            logDebug("Quests get: %s", userQuestModels);
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private long getTimeToNextQuest(long timestampRemote) {
    long timeDifference = new Date().getTime() - timestampRemote;
    long remainingTimeToQuest = TimeUtils.minsToMillis(Constants.TIME_TO_QUEST_MINS) - timeDifference;

    return remainingTimeToQuest;
  }

  private void initTimerMillis(Long time) {
    if (time != null && !adapter.isAdapterFullFilled()) {
      tvRemainingTimeLabel.setText(R.string.fragment_quests_remaining_time);

      timer.shutdown();
      timer = Executors.newSingleThreadScheduledExecutor();
      AtomicLong timeToNewQuest = new AtomicLong(time);
      logDebug("Init timer: %s", time);

      timer.scheduleAtFixedRate(() -> {
        if (timeToNewQuest.get() > 0) {
          Long remainingMins = timeToNewQuest.get() / 1000 / 60;
          Long remainingSecs = timeToNewQuest.get() / 1000 - remainingMins * 60;

          String remainingTimeString = String.format(Locale.getDefault(),
              getString(R.string.fragment_quests_remaining_time),
              remainingMins,
              remainingSecs);
          tvRemainingTimeLabel.setText(remainingTimeString);
        } else {
          timer.shutdown();
          tvRemainingTimeLabel.setText(getString(R.string.loading));
          logDebug("Request quest");
          questsViewModel.requestQuest(availableQuests);
          questsViewModel.initLastRequestedQuestTimestamp(
              lastQuestTimestamp + TimeUtils.minsToMillis(Constants.TIME_TO_QUEST_MINS)
          );
        }

        timeToNewQuest.addAndGet(-1000);
      }, 0, 1, TimeUnit.SECONDS);
    } else {
      tvRemainingTimeLabel.setText(getString(R.string.fragment_quests_quest_max));
      timer.shutdown();
    }
  }
}
