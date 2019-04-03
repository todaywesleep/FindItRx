package pro.papaya.canyo.finditrx.adapter;

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
import pro.papaya.canyo.finditrx.model.firebase.UserQuestsModel;

public class UserQuestsAdapter extends RecyclerView.Adapter<UserQuestsAdapter.ViewHolder> {
  private List<UserQuestsModel> data;

  public UserQuestsAdapter() {
    this.data = new ArrayList<>();
  }

  public UserQuestsAdapter(List<UserQuestsModel> data) {
    this.data = data;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_user_quest, parent, false);

    return new ViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UserQuestsModel model = data.get(position);
    holder.questName.setText(model.getLabel());
    holder.questRevard.setText(model.getReward());
  }

  public void setData(List<UserQuestsModel> data) {
    this.data = data;
    notifyDataSetChanged();
  }

  public List<UserQuestsModel> getData() {
    return data;
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_user_quest_title)
    TextView questName;
    @BindView(R.id.item_user_quest_reward)
    TextView questRevard;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
