package pro.papaya.canyo.finditrx.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.TimestampModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.utils.TimeUtils;
import pro.papaya.canyo.finditrx.viewmodel.QuestsViewModel;

public class QuestsFragment extends BaseFragment implements UserQuestsAdapter.QuestCallback {
  private static QuestsFragment INSTANCE = null;

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
  private Long timestamp;

  private boolean isTimestampEventSubscribed = false;

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
    rvActiveQuests.addItemDecoration(getItemDecorator());
    adapter = new UserQuestsAdapter();
    adapter.setCallback(this);
    rvActiveQuests.setAdapter(adapter);

    subscribeToAllLabels();
    subscribeToQuestTimestamp();
  }

  @Override
  public void onQuestClicked(UserQuestModel quest) {
    logDebug("Quest clicked %s", quest.getIdentifier());
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
    questsViewModel.getItemCollectionModel()
        .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            List<QuestModel> questModels = queryDocumentSnapshots.toObjects(QuestModel.class);
            availableQuests.clear();
            availableQuests.addAll(questModels);

            for (UserQuestModel userQuest : adapter.getData()) {
              availableQuests.remove(QuestModel.from(userQuest));
            }

            logDebug("Items collection get: %s", questModels);
          }

          @Override
          public void onError(FirebaseFirestoreException e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private void subscribeToQuestTimestamp() {
    questsViewModel.getLastRequestedQuestReference()
        .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            TimestampModel timestampModel = documentSnapshot.toObject(TimestampModel.class);

            if (timestampModel != null) {
              initTimerMillis(getTimeToNextQuest(timestampModel.getLastRequestedQuestTime()));
            } else {
              long lastRequestedQuestTime = new Date().getTime();
              logDebug("Init last requested quest: %s", lastRequestedQuestTime);
              questsViewModel.initLastRequestedQuestTimestamp(lastRequestedQuestTime);
            }
          }

          @Override
          public void onError(FirebaseFirestoreException e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private long getTimeToNextQuest(long timestampRemote) {
    long timeDifference = new Date().getTime() - timestampRemote;
    long remainingTimeToQuest = TimeUtils.minsToMillis(Constants.TIME_TO_QUEST_MINS) - timeDifference;
    logDebug("Init timer: %s", remainingTimeToQuest);

    return remainingTimeToQuest;
  }

  private void initTimerMillis(Long time) {
    if (time != null && !adapter.isAdapterFullFilled()) {
      tvRemainingTimeLabel.setText(R.string.fragment_quests_remaining_time);

      ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
      AtomicLong timeToNewQuest = new AtomicLong(time);

      ses.scheduleAtFixedRate(() -> {
        if (timeToNewQuest.get() > 0) {
          Long remainingMins = timeToNewQuest.get() / 1000 / 60;
          Long remainingSecs = timeToNewQuest.get() / 1000 - remainingMins * 60;

          String remainingTimeString = String.format(Locale.getDefault(),
              getString(R.string.fragment_quests_remaining_time),
              remainingMins,
              remainingSecs);
          tvRemainingTimeLabel.setText(remainingTimeString);
        } else {
          ses.shutdown();
          tvRemainingTimeLabel.setText("Loading...");
        }

        timeToNewQuest.addAndGet(-1000);
      }, 0, 1, TimeUnit.SECONDS);
    } else {
      tvRemainingTimeLabel.setText(getString(R.string.fragment_quests_quest_max));
    }
  }
}
