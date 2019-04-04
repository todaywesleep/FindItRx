package pro.papaya.canyo.finditrx.adapter;

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
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class UserQuestsAdapter extends RecyclerView.Adapter<UserQuestsAdapter.ViewHolder> {
  private List<UserQuestModel> data;

  public UserQuestsAdapter() {
    this.data = new ArrayList<>();
  }

  public UserQuestsAdapter(List<UserQuestModel> data) {
    this.data = data;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_user_quest, parent, false);

    return new ViewHolder(rootView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UserQuestModel model = data.get(position);
    holder.questName.setText(model.getLabel());
    holder.questRevard.setText(Integer.toString(model.getReward()));
  }

  public void setData(List<UserQuestModel> data) {
    this.data = data;
    notifyDataSetChanged();
  }

  public List<UserQuestModel> getData() {
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
