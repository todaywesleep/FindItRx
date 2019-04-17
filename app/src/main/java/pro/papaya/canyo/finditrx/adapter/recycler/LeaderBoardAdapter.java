package pro.papaya.canyo.finditrx.adapter.recycler;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import timber.log.Timber;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
  private List<UserModel> data = new ArrayList<>();

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_leader_board_user, parent, false);

    return new ViewHolder(rootView);
  }

  @Override
  @SuppressLint("SetTextI18n")
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UserModel model = data.get(position);
    holder.userIdx.setText(Integer.toString(position + 1));
    holder.userName.setText(model.getNickName());
    holder.score.setText(Integer.toString(model.getLevel()));

    holder.setOnClickListener(v -> {
      Timber.d("TEST item %s clicked", model.getNickName());
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void setData(List<UserModel> newData) {
    data.clear();
    data.addAll(newData);

    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    @BindView(R.id.item_leader_board_user_idx)
    TextView userIdx;
    @BindView(R.id.item_leader_board_user_name)
    TextView userName;
    @BindView(R.id.item_leader_board_score)
    TextView score;

    public void setOnClickListener(View.OnClickListener onClickListener) {
      rootView.setOnClickListener(onClickListener);
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      rootView = itemView;
      ButterKnife.bind(this, itemView);
    }
  }
}
