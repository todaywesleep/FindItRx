package pro.papaya.canyo.finditrx.fragment.leaderboard;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.recycler.LeaderBoardAdapter;
import pro.papaya.canyo.finditrx.firebase.FireBaseUsersManager;
import pro.papaya.canyo.finditrx.fragment.main.BaseFragment;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;

public abstract class BaseLeaderBoardFragment extends BaseFragment {
  @BindView(R.id.leader_board_recycler)
  RecyclerView recyclerView;
  @BindView(R.id.leader_board_type)
  TextView leaderBoardTypeLabel;

  protected LeaderBoardPagerModel model;
  protected LeaderBoardAdapter adapter;

  protected abstract Observer<List<UserModel>> getObserver();

  public BaseLeaderBoardFragment() {
    if (this instanceof LevelFragment) {
      model = LeaderBoardPagerModel.LEVEL_PAGE;
    } else if (this instanceof BalanceFragment) {
      model = LeaderBoardPagerModel.BALANCE_PAGE;
    } else {
      model = LeaderBoardPagerModel.NEW_SUBJECTS_PAGE;
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_leader_board_base, container, false);
    ButterKnife.bind(this, view);
    initViews();
    subscribeToModel();

    return view;
  }

  protected void initViews() {
    adapter = new LeaderBoardAdapter(model, getContext());

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.addItemDecoration(getItemDecorator());
    recyclerView.setAdapter(adapter);

    leaderBoardTypeLabel.setText(getString(R.string.leader_board_type, getTypeLabel()));
  }

  protected void subscribeToModel() {
    FireBaseUsersManager.getInstance().getUsersCollection(model)
        .subscribe(getObserver());
  }

  protected String getTypeLabel() {
    int stringTypeRes = -1;

    switch (model) {
      case LEVEL_PAGE: {
        stringTypeRes = R.string.leader_board_level_tab;
        break;
      }

      case BALANCE_PAGE: {
        stringTypeRes = R.string.leader_board_money_tab;
        break;
      }

      case NEW_SUBJECTS_PAGE: {
        stringTypeRes = R.string.leader_board_subjects_tab;
        break;
      }
    }

    return getString(stringTypeRes);
  }

  private RecyclerView.ItemDecoration getItemDecorator() {
    return new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int defaultOffset = getResources().getDimensionPixelSize(R.dimen.padding_default);

        if (parent.getChildAdapterPosition(view) == 0) {
          outRect.top = defaultOffset;
        }

        outRect.bottom = defaultOffset;
        outRect.left = defaultOffset;
        outRect.right = defaultOffset;
      }
    };
  }
}
