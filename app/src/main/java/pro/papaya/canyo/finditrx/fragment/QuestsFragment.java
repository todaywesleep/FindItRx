package pro.papaya.canyo.finditrx.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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
import pro.papaya.canyo.finditrx.firebase.FireBaseProfileManager;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.viewmodel.QuestsViewModel;

public class QuestsFragment extends BaseFragment {
  public static QuestsFragment INSTANCE = null;

  public interface QuestFragmentCallback {
  }

  @BindView(R.id.fragment_quests_rv)
  RecyclerView rvActiveQuests;

  private QuestsViewModel questsViewModel;
  private UserQuestsAdapter adapter;
  private QuestFragmentCallback callback;
  private List<QuestModel> availableQuests = new ArrayList<>();
  private Long timestamp;

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
    rvActiveQuests.setAdapter(adapter);

//    getTimestampEvent();
//    subscribeToAllLabels();
  }

  public void setCallback(QuestFragmentCallback callback) {
    this.callback = callback;
  }

  private void subscribeToUserQuests() {
    questsViewModel.getObservableUserQuests()
        .subscribe(new Observer<List<UserQuestModel>>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(List<UserQuestModel> userQuestModels) {
            logDebug("Get user quests: %s", userQuestModels);
            adapter.setData(userQuestModels);
            requestUserQuests(userQuestModels);
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

  private void getTimestampEvent() {
    FireBaseProfileManager.getInstance().getTimeStampEvent()
        .subscribe(new SingleObserver<Long>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onSuccess(Long timestampRemote) {
            timestamp = timestampRemote;
            subscribeToUserQuests();
          }

          @Override
          public void onError(Throwable e) {
            subscribeToUserQuests();
            timestamp = null;
            logError(e);
          }
        });
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
//    questsViewModel.getAllLabels()
//        .subscribe(new Observer<List<QuestModel>>() {
//          @Override
//          public void onSubscribe(Disposable d) {
//          }
//
//          @Override
//          public void onNext(List<QuestModel> questModels) {
//            availableQuests.clear();
//            availableQuests.addAll(questModels);
//
//            for (UserQuestModel userQuest : adapter.getData()) {
//              availableQuests.remove(QuestModel.from(userQuest));
//            }
//
//            logDebug("Items collection get: %s", questModels);
//          }
//
//          @Override
//          public void onError(Throwable e) {
//            showSnackBar(e.getLocalizedMessage());
//            logError(e);
//          }
//
//          @Override
//          public void onComplete() {
//          }
//        });
  }

  private void requestUserQuests(List<UserQuestModel> userQuests) {
    if (userQuests.size() < Constants.USER_MAX_QUESTS) {
      questsViewModel.requestQuests(availableQuests, timestamp, userQuests.size())
          .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
              questsViewModel.setTimestamp(new Date().getTime());
              logDebug("Quests successfully requested");
            }

            @Override
            public void onError(Throwable e) {
              showSnackBar(e.getLocalizedMessage());
              logError(e);
            }
          });
    }
  }
}
