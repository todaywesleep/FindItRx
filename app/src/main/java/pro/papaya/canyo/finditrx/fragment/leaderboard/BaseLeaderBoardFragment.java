package pro.papaya.canyo.finditrx.fragment.leaderboard;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

  protected LeaderBoardPagerModel model;
  protected LeaderBoardAdapter adapter;

  protected abstract Observer<List<UserModel>> getObserver();

  public BaseLeaderBoardFragment() {
    if (this instanceof LevelFragment) {
      model = LeaderBoardPagerModel.LEVEL_PAGE;
    } else if (this instanceof BalanceFragment) {
      model = LeaderBoardPagerModel.MONEY_PAGE;
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
    adapter = new LeaderBoardAdapter();

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.addItemDecoration(getItemDecorator());
    recyclerView.setAdapter(adapter);
  }

  protected void subscribeToModel() {
    FireBaseUsersManager.getInstance().getUsersCollection(model)
        .subscribe(getObserver());
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
}
