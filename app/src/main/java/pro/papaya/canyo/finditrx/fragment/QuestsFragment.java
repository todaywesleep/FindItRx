package pro.papaya.canyo.finditrx.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

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
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.UserQuestsAdapter;
import pro.papaya.canyo.finditrx.listener.ExtendedEventListener;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
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
    rvActiveQuests.setAdapter(adapter);

    subscribeToAllLabels();
    subscribeToUserQuests();
  }

  public void setCallback(QuestFragmentCallback callback) {
    this.callback = callback;
  }

  private void subscribeToUserQuests() {
    questsViewModel.getQuestsReference()
        .addSnapshotListener(new ExtendedEventListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            List<UserQuestModel> userQuestModels = queryDocumentSnapshots.toObjects(UserQuestModel.class);
            adapter.setData(userQuestModels);
            subscribeToTimestampEvent();
            logDebug("Get user quests: %s", userQuestModels);
          }

          @Override
          public void onError(FirebaseFirestoreException e) {
            subscribeToTimestampEvent();
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private void subscribeToTimestampEvent() {
    if (!isTimestampEventSubscribed) {
      isTimestampEventSubscribed = true;

      questsViewModel.getUserReference()
          .addSnapshotListener(new ExtendedEventListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              if (documentSnapshot != null) {
                UserModel remoteUserModel = documentSnapshot.toObject(UserModel.class);
                timestamp = null;
                if (remoteUserModel != null) {
                  timestamp = remoteUserModel.getQuestTimestamp();
                  if (timestamp == null) {
                    requestUserQuests(adapter.getData());
                  }
                } else {
                  showSnackBar("User model is null");
                  logError(new Throwable("User model is null"));
                }
              } else {
                showSnackBar("User model snapshot is null");
                logError(new Throwable("User model snapshot is null"));
              }
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
              timestamp = null;
              showSnackBar(e.getLocalizedMessage());
              logError(e);
            }
          });
    }
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

  private void requestUserQuests(List<UserQuestModel> userQuests) {
    if (userQuests.size() < Constants.USER_MAX_QUESTS) {
      logDebug("Request quests");
      questsViewModel.requestQuests(availableQuests, timestamp, userQuests.size());
      questsViewModel.setTimestamp(new Date().getTime());
    }
  }
}
