package pro.papaya.canyo.finditrx.adapter.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.model.local.QuestRarity;
import pro.papaya.canyo.finditrx.utils.Constants;

import static pro.papaya.canyo.finditrx.utils.Constants.CLOSED_BOLD_TAG;
import static pro.papaya.canyo.finditrx.utils.Constants.OPENED_BOLD_TAG;

public class UserQuestsAdapter extends RecyclerView.Adapter<UserQuestsAdapter.ViewHolder> {
  public interface QuestCallback {
    void onQuestClicked(UserQuestModel quest);
  }

  private List<UserQuestModel> data;
  private QuestCallback callback;
  private boolean isInitialized = false;
  private Context context;

  public UserQuestsAdapter(Context context) {
    this.data = new ArrayList<>();
    this.context = context;
  }

  public void setCallback(QuestCallback callback) {
    this.callback = callback;
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
    applyCardColor(holder, model);
    holder.questName.setText(model.getLabel());
    setupReward(
        R.string.fragment_quests_coins,
        model.getReward(),
        holder.questReward
    );
    setupReward(
        R.string.fragment_quests_exp,
        model.getExperience(),
        holder.questRewardExperience
    );

    holder.setOnClickListener(v -> {
      if (callback != null) {
        callback.onQuestClicked(data.get(position));
      }
    });
  }

  private void setupReward(int rewardRes, Integer value, TextView view) {
    String labelRes = OPENED_BOLD_TAG
        .concat(context.getString(rewardRes))
        .concat(CLOSED_BOLD_TAG);

    view.setText(Html.fromHtml(labelRes.concat(value.toString())));
  }

  private void applyCardColor(@NonNull ViewHolder holder, UserQuestModel model) {
    QuestRarity rarity = QuestRarity.getRarity(model.getReward());
    GradientDrawable bgShape = (GradientDrawable) holder.rootView.getBackground();
    bgShape.setColor(ContextCompat.getColor(context, rarity.getColor()));
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public boolean isInitialized() {
    return isInitialized;
  }

  public void setData(List<UserQuestModel> data) {
    if (!isInitialized) {
      isInitialized = true;
    }

    this.data = data;
    notifyDataSetChanged();
  }

  public List<UserQuestModel> getData() {
    return data;
  }

  public boolean isAdapterFullFilled() {
    return data != null && data.size() >= Constants.USER_MAX_QUESTS;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    @BindView(R.id.item_user_quest_title)
    TextView questName;
    @BindView(R.id.item_user_quest_reward)
    TextView questReward;
    @BindView(R.id.item_user_quest_reward_exp)
    TextView questRewardExperience;

    public void setOnClickListener(View.OnClickListener onClickListener) {
      rootView.setOnClickListener(onClickListener);
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      rootView = itemView.findViewById(R.id.item_user_quest_root);
      ButterKnife.bind(this, itemView);
    }
  }
}
